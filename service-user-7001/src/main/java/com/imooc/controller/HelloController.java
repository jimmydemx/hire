package com.imooc.controller;

import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags="Users")
@RestController
@RequestMapping("ur")
public class HelloController {

    @Autowired
    private StuService stuService;

    @ApiOperation("hello_world")
    @GetMapping("/hello")
    public Object hello() {
        return "hello UserService....";
    }

    @ApiOperation("add")
    @GetMapping("add")
    public Object add(){
        Stu stu = new Stu();
        stu.setId(1002);
        stu.setAge(10);
        stu.setName("tom");
        stuService.save(stu);

        return "add new stu";
    }
}
