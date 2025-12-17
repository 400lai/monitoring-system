package com.laiiiii.controller;

import com.laiiiii.domain.Emp;
import com.laiiiii.domain.LoginInfo;
import com.laiiiii.domain.Result;
import com.laiiiii.service.EmpService;
import com.laiiiii.util.RateLimiterUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private EmpService empService;

    @PostMapping
    public ResponseEntity<Result> login(@RequestBody Emp emp, HttpServletRequest request) {
        // 1. 获取客户端 IP
        String clientIp = getClientIpAddress(request);
        log.debug("登录请求来自 IP: {}", clientIp);

        // 2. IP 限流检查：每分钟最多5次
        if (!RateLimiterUtil.tryAcquireIp(clientIp)) {
            log.warn("IP {} 触发登录限流", clientIp);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Result.error("请求过于频繁，请1分钟后重试"));
        }

        // 3. 执行登录逻辑
        LoginInfo loginInfo = empService.login(emp);
        if (loginInfo != null) {
            return ResponseEntity.ok(Result.success(loginInfo));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Result.error("用户名或密码错误"));
        }
    }

    /**
     * 获取真实客户端 IP（支持代理、Nginx 等）
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            // 多个 IP 时取第一个
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }
}
