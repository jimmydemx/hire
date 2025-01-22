package com.imooc.utils;


import com.google.gson.Gson;
import com.imooc.exceptions.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class jwtUtils {

    @Autowired
    private jwtProperties jwtProperties;

    public static final String at = "@";
    public String generatorJwt(String body, SecretKey secretKey){
        String jwtToken = Jwts.builder()
                .setSubject(body)
                .signWith(secretKey)
                .compact();

        return jwtToken;
    }

    public String generatorJwt(String body, Long expireTimes, SecretKey secretKey){
        // 定义过期时间
        Date expireDate = new Date(System.currentTimeMillis() + expireTimes);
        String jwtToken = Jwts.builder().setSubject(body)
                .signWith(secretKey)
                .setExpiration(expireDate)
                .compact();
        return jwtToken;
    }

    public String createJwtWithPrefix(String body,String prefix){
        return prefix +  at + handleJwt(body, null);
    }

    public String createJwtWithPrefix(String body,Long expireTimes,String prefix){
        if(expireTimes == null){
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);
        }
        return  prefix +  at + handleJwt(body, expireTimes);
    }

    public String createJwt(String body){
        return handleJwt(body, null);
    }

    public String createJwt(String body,Long expireTimes){
        if(expireTimes == null){
            GraceException.display(ResponseStatusEnum.SYSTEM_NO_EXPIRE_ERROR);
        }
        return handleJwt(body, expireTimes);
    }

    public String handleJwt(String body, Long expireTimes){
        String userKey = jwtProperties.getKey();
        // 对于密钥进行base64编码
        String base64 = Base64.getEncoder().encodeToString(userKey.getBytes());

        // 2，对于base64生成一个密钥对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());

        String jwt="";
        if(expireTimes!=null){
            jwt = generatorJwt(body,expireTimes,secretKey);
        }else {
            jwt= generatorJwt(body,secretKey);
        }
        log.info("jwtutils- handleJwt -jwt  =",jwt);
        return jwt;

    }


    public String checkJWT(String pedningJWT){
        String user_key = jwtProperties.getKey();

        String base64 = Base64.getEncoder().encodeToString(user_key.getBytes());

        // 2，对于base64生成一个密钥对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());
        // VALIDATE JWT
        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey).build(); //构造解析器

        // 可以获得Claims,从而去get相关数据，如果抛出异常，说明解析不通过，也就是token失效了
        Jws<Claims> jws = jwtParser.parseClaimsJws(pedningJWT );
        String body = jws.getBody().getSubject();
        return body;


    }





}
