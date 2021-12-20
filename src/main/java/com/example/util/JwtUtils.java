package com.example.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    /**
     * 密钥
     */
    public static final String SECRET = "secret_password";
    /**
     * token过期时间
     */
    public static final int EXPIRE = 5;

    /**
     * 生成token
     */
    public static String createToken(String userId, String username) throws UnsupportedEncodingException {
        // token由三部分组成 header, payload, signature
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, EXPIRE);
        Date expireDate = nowTime.getTime();

        // Header部分
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256"); // 签名的算法
        map.put("typ", "JWT"); // 令牌的类型

        // payload部分
        // iss 签发人 exp 过期时间 sub 主题 aud 受众
        // nbf 生效时间 iat 签发时间 jti 编号
        String token = JWT.create()
                .withHeader(map)
                .withClaim("userId", userId)
                .withClaim("userName", username)
                .withSubject("测试")
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    /**
     * 验证token
     */
    public static Map<String, Claim> verifyToken(String token) throws UnsupportedEncodingException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException("验证失败");
        }
        return jwt.getClaims();
    }

    /**
     * 解析token
     */
    public static Map<String, Claim> parseToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims();
    }

    public static void main(String[] args) {
        try {
            String token = JwtUtils.createToken("123456", "admin");
            System.out.println("token = " + token);

            Map<String, Claim> map = JwtUtils.verifyToken(token);
            for (Map.Entry<String, Claim> entry : map.entrySet()) {
                if (entry.getValue().asString() != null) {
                    System.out.println(entry.getKey() + "  " + entry.getValue().asString());
                } else {
                    System.out.println(entry.getKey() + "   " + entry.getValue().asDate());
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
