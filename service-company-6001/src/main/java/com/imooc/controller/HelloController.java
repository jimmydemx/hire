package com.imooc.controller;

import com.imooc.pojo.Stu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("u")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        Stu stu = new Stu("imooc",1001,18);
        log.info("info:"+stu.toString());
        log.debug("debug:"+stu.toString());
        log.error("error:"+stu.toString());
        return "hello UserService....";
    }
}
