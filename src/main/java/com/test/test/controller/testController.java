package com.test.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.test.test1.repository.test1Mapper;
import com.test.test.test2.repository.test2Mapper;

import lombok.extern.slf4j.Slf4j;

import com.test.test.service.*;

@RestController
@Slf4j
@RequestMapping(value = "/rest")
public class testController {

    @Autowired
    private test1Mapper test1Mapper;

    @Autowired
    private test2Mapper test2Mapper;

    @Autowired
    private testService testService;

    @GetMapping(value = "/test")
    public void test() {
        System.out.println(test1Mapper.test1() + ">>>11");
        System.out.println(test2Mapper.test2() + ">>>22");

    }

    @GetMapping(value = "/test2")
    public void test2() {
        log.info("Batch Start!!!");
        testService.dataMigrationJob();
        log.info("Batch End!!!");

    }
}
