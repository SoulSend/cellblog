package org.hrc.backblog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT工具类，生成token，和解析token
 */
public class JwtUtil {
    private static final String SECRET_KEY = "CellBlog";
    private static final long EXPIRATION_TIME = 86400000; // 1天的过期时间（毫秒）

    /**
     * 创建token
     * @param userId 传入用户id作为token的负载
     * @return 返回一个token
     */
    public static String createToken(String userId) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + EXPIRATION_TIME);

        JwtBuilder builder = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        return builder.compact();
    }

    /**
     * 验证token
     * @param token 传入需要验证的token
     * @return 返回token的负载 userId
     */
    public static String parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // 返回负载中的用户 ID
    }
}
