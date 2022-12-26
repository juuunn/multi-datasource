package com.test.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.test.test1.repository.test1Mapper;
import com.test.test.test2.repository.test2Mapper;

@RestController
@RequestMapping(value = "/rest")
public class testController {

    @Autowired
    private test1Mapper test1Mapper;

    @Autowired
    private test2Mapper test2Mapper;

    @GetMapping(value = "/test")
    public void test() {
        System.out.println(test1Mapper.test1() + ">>>11");
        System.out.println(test2Mapper.test2() + ">>>22");

    }
}
