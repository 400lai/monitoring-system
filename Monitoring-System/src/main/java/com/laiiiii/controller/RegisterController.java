package com.laiiiii.controller;

import com.laiiiii.domain.RegisterInfo;
import com.laiiiii.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/register")
@RestController
public class RegisterController {
    @Autowired
    private EmpService empService;

    @PostMapping
    public String register(@RequestBody RegisterInfo registerInfo) {
        if (empService.register(registerInfo)) {
            return "注册成功";
        } else {
            return "注册失败，请检查输入信息";
        }
    }
}
