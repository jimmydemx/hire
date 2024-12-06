package com.imooc.controller;

import com.imooc.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("c")
@Slf4j
public class HelloController {


    @Autowired
    private SMSUtils SMSUtils;
    @Value("${server.port}")
    private String port;


    @GetMapping("sms")
    public Object sendSMS(){
        SMSUtils.sendSMS(1882342342,"2342");
        return "SEND SMS OKAY...";
    }

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
