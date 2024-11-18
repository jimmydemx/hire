package com.imooc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

//使用了 lombok.Data就不需要直接写 set/get，
// AllArgsConstructor 表示创建时候必须传入参数 
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Stu {
    private String name;
    private Integer id;
    private Integer age;

}
