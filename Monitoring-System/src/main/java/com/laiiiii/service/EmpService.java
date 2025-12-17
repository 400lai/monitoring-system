package com.laiiiii.service;

import com.laiiiii.domain.Emp;
import com.laiiiii.domain.LoginInfo;
import com.laiiiii.domain.RegisterInfo;

public interface EmpService {


    /**
     * 员工登录
     */
    LoginInfo login(Emp emp);

    /**
     * 用户注册
     */
    boolean register(RegisterInfo registerInfo);
}
