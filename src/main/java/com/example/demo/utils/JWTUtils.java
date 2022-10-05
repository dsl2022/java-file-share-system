package com.example.demo.utils;
import com.example.demo.dto.JwtUserPayload;
import com.nimbusds.jose.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;

public class JWTUtils {
    private static Logger logger = LoggerFactory.getLogger(JWTUtils.class);
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static String generateToken(Integer id,String username, String email,String jwtKey) {
//        Key key = Keys.hmacShaKeyFor(jwtKey.getBytes());

        logger.debug(String.valueOf(key));
        return Jwts.builder().setSubject(username).claim("userId",id).claim("email",email).signWith(key,SignatureAlgorithm.HS512).compact();
    }

    public static JwtUserPayload parseToken(String clientToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(clientToken).getBody();



        JwtUserPayload jwtUserPayload = new JwtUserPayload();
        jwtUserPayload.setId(Integer.valueOf(claims.get("userId").toString()));
        jwtUserPayload.setEmail(claims.get("email").toString());
        // subject is username
        jwtUserPayload.setUsername(claims.getSubject());
         return jwtUserPayload;
    }
    public static boolean verifyToken(String clientToken) {

//        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(clientToken).getBody();

        return true;
    }
}
