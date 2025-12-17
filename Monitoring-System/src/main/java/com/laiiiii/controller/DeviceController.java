package com.laiiiii.controller;

import com.laiiiii.domain.Device;
import com.laiiiii.domain.Result;
import com.laiiiii.service.DeviceService;
import com.laiiiii.util.JwtUtils;
import com.laiiiii.util.RateLimiterUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 根据ID获取设备详情（需JWT认证 + 用户级限流）
     */
    @GetMapping("/{id}")
    public Result getDeviceById(@PathVariable Long id, HttpServletRequest request) {
        try {
            // 1. 从 Header 获取 Token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("缺少有效Token");
            }

            // 2. 解析 JWT 获取用户信息
            Claims claims = JwtUtils.parseJwt(authHeader);
            Integer userId = (Integer) claims.get("id");
            if (userId == null) {
                return Result.error("Token中缺少用户ID");
            }

            // 3. 限流检查：每个用户每小时最多3次
            if (!RateLimiterUtil.tryAcquireUser(userId)) {
                // 返回 429 状态码（需通过 ResponseEntity 控制）
                // 但你要求用 Result，所以我们仍返回 Result，并在文档中说明应返回 429
                // 若需严格返回 429，请改用 ResponseEntity（见下方注释）
                return Result.error("请求过于频繁，请1小时后再试");
            }

            // 4. 查询设备
            Device device = deviceService.getDeviceById(id);
            if (device == null) {
                return Result.error("设备不存在");
            }

            return Result.success(device);

        } catch (Exception e) {
            log.warn("设备查询失败: {}", e.getMessage());
            return Result.error("认证失败或Token无效");
        }
    }

    /*
     * 【可选】如果你希望严格返回 HTTP 429，可改用以下方式：
     *
     * @GetMapping("/{id}")
     * public ResponseEntity<Result> getDeviceById(...) {
     *     ...
     *     if (!RateLimiterUtil.tryAcquire(userId)) {
     *         return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
     *                 .body(Result.error("请求过于频繁"));
     *     }
     *     ...
     *     return ResponseEntity.ok(Result.success(device));
     * }
     */
}