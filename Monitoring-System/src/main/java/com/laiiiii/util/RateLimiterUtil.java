package com.laiiiii.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简易内存限流器：每个用户每小时最多3次请求
 */
public class RateLimiterUtil {

    // ===== 用户级限流（用于 /devices/{id}）=====
    private static final ConcurrentHashMap<String, RequestInfo> userRequestMap = new ConcurrentHashMap<>();

    // ===== IP级限流（用于 /login）=====
    private static final ConcurrentHashMap<String, RequestInfo> ipRequestMap = new ConcurrentHashMap<>();

    private static class RequestInfo {
        AtomicInteger count;
        LocalDateTime firstRequestTime;

        RequestInfo() {
            this.count = new AtomicInteger(1);
            this.firstRequestTime = LocalDateTime.now();
        }
    }

    /**
     * 用户级限流：每小时最多3次
     */
    public static boolean tryAcquireUser(Integer userId) {
        if (userId == null) return false;
        return tryAcquire(userRequestMap, String.valueOf(userId), 60, 3);
    }

    /**
     * IP级限流：每分钟最多5次
     */
    public static boolean tryAcquireIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        return tryAcquire(ipRequestMap, ip, 1, 5); // 1分钟，5次
    }

    /**
     * 通用限流逻辑
     * @param map 存储请求记录的 Map
     * @param key 限流键（如 IP 或 userId）
     * @param windowMinutes 时间窗口（分钟）
     * @param maxRequests 最大允许请求次数
     * @return true 表示允许，false 表示被限流
     */
    private static boolean tryAcquire(
            ConcurrentHashMap<String, RequestInfo> map,
            String key,
            int windowMinutes,
            int maxRequests
    ) {
        RequestInfo info = map.compute(key, (k, existing) -> {
            if (existing == null) {
                return new RequestInfo(); // 首次请求
            }

            long minutesPassed = ChronoUnit.MINUTES.between(existing.firstRequestTime, LocalDateTime.now());
            if (minutesPassed >= windowMinutes) {
                // 窗口过期，重置
                return new RequestInfo();
            }

            // 窗口内，计数+1
            existing.count.incrementAndGet();
            return existing;
        });

        return info.count.get() <= maxRequests;
    }
}
