package com.imooc.test;

import com.google.gson.Gson;
import com.imooc.pojo.Stu;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecretKeyBuilder;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootTest
public class JWTTest {

    // 自定义密钥，提供给jwt加密，一般都是由开发者或者公司定的规范,user_key一定要有一定长度，否则不满足Keys.hmacShaKeyFor的长度
    public static final String user_key= "imooc_123456789_123456789";
    @Test
    public void createJWT(){
        // 1， 对密钥影响base64编码
//        SecretKey secretKey = Jwts.SIG.HS256.key().build();
//        byte[] base64 = Decoders.BASE64.decode(user_key);
        String base64 = Base64.getEncoder().encodeToString(user_key.getBytes());
        
        // 2，对于base64生成一个密钥对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());
        // 3，通过jwt生成token字符串
        Stu stu = new Stu(1001,"imooc", 18);
        String stuJson = new Gson().toJson(stu);

        String myJWT = Jwts.builder().setSubject(stuJson) //设置用户自定义数据
                .signWith(secretKey) // 使用密钥对象进行jwt的生成
                .compact();// 压缩生成jwt

        System.out.println(myJWT);
    }


    @Test
    public void checkJWT(){
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEwMDEsXCJuYW1lXCI6XCJpbW9vY1wiLFwiYWdlXCI6MTh9In0.7Eb3iFfCYfQzfSQIeAXDqsqD19AeasD6pr54vetjij0";

        String base64 = Base64.getEncoder().encodeToString(user_key.getBytes());

        // 2，对于base64生成一个密钥对象
        SecretKey secretKey = Keys.hmacShaKeyFor(base64.getBytes());
        // VALIDATE JWT
        JwtParser jwtParser = Jwts.parser().setSigningKey(secretKey).build(); //构造解析器

        // 可以获得Claims,从而去get相关数据，如果抛出异常，说明解析不通过，也就是token失效了
        Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);
        String stuJson = jws.getBody().getSubject();

        Stu stu = new Gson().fromJson(stuJson, Stu.class);
        System.out.println(stu);

    }
}
