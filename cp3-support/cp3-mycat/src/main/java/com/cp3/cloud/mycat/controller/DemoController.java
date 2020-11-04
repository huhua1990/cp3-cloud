package com.cp3.cloud.mycat.controller;

import com.cp3.cloud.mycat.entity.UserDemo;
import com.cp3.cloud.mycat.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/23 18:28
 * @Version:1.0
 */
@RestController
@RequestMapping("/mycat/userDemo")
public class DemoController {
    @Autowired
    DemoService demoService;

    @PostMapping("/save")
    public void save(@RequestBody UserDemo userDemo){
        demoService.save(userDemo);
    }
    @GetMapping("/findList")
    public List<UserDemo> findList(){
        return demoService.findList();
    }
}
