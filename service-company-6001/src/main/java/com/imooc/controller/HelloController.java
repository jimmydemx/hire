package com.imooc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("c")
@Slf4j
public class HelloController {

    @Value("${server.port}")
    private String port;


    @GetMapping("/hello")
    public Object hello() {
//        Stu stu = new Stu("imooc",1001,18);
//        log.info("info:"+stu.toString());
//        log.debug("debug:"+stu.toString());
//        log.error("error:"+stu.toString());
        log.info("当前的端口号是：",port);
        return "hello UserService....";
    }
}
