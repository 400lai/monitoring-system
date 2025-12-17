package com.laiiiii.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 使用安全的密钥生成方式
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final Long expire = 43200000L; // 12小时

    /**
     * 生成JWT Token
     * @param claims 要存储的用户信息
     * @return JWT Token字符串
     */
    public static String generateJwt(Map<String,Object> claims){
        // 添加过期时间字段，格式为 yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        claims.put("expireTime", sdf.format(new Date(System.currentTimeMillis() + expire)));

        return Jwts.builder()
                .addClaims(claims)
                .signWith(secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .setIssuedAt(new Date())
                .compact();
    }


    /**
     * 安全解析并验证 JWT，自动处理过期、签名错误等
     */
    public static Claims parseJwt(String token) throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", "").trim())
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token已过期");
        } catch (JwtException e) {
            throw new Exception("无效Token");
        }
    }
}
