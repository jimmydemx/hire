package com.imooc.controller;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Stu;
import com.imooc.pojo.Users;
import com.imooc.utils.jwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="Auths")
@RestController
@RequestMapping("auth")
@Slf4j
public class AuthController {


    @Autowired
    private jwtUtils jwtUtils;

    @ApiOperation("hello")
    @GetMapping("hello")
    public Object hello(){
        return "auth service";
    }

    @ApiOperation("create JWT token")
    @GetMapping("jwt")
    public Object createJWT(){
        Stu stu = new Stu(1001,"imooc", 18);
        String jwt = jwtUtils.createJwtWithPrefix(new Gson().toJson(stu), Long.valueOf(1000), "");
        return GraceJSONResult.ok(jwt);
    }
}
