package com.laiiiii.service.impl;

import com.laiiiii.domain.Emp;
import com.laiiiii.domain.LoginInfo;
import com.laiiiii.domain.RegisterInfo;
import com.laiiiii.mapper.EmpMapper;
import com.laiiiii.service.EmpService;
import com.laiiiii.util.JwtUtils;
import com.laiiiii.util.PasswordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public LoginInfo login(Emp emp){
        // 1.调用mapper接口，根据用户名和密码查询员工信息
        Emp e = empMapper.selectByUsernameAndPassword(emp);

        // 2.判断：判断是否存在这个员工，如果存在，组装登录成功信息
        if(e != null){
            log.info("登录成功，员工信息：{}",e);

            // 生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id",e.getId());                 // 员工ID
            claims.put("username",e.getUsername());
            claims.put("job", e.getJob());              // 添加员工职位信息

            String jwt = JwtUtils.generateJwt(claims);

            return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
        }


        // 3.不存在，返回null
        return null;
    }

    @Override
    public boolean register(RegisterInfo registerInfo) {
        // 验证密码复杂度
        if (!PasswordValidator.isValid(registerInfo.getPassword())) {
            log.warn("密码不符合复杂度要求");
            return false;
        }

        // 检查用户名是否已存在（这里只是一个示例，实际应用中需要更详细的检查）
        if (empMapper.selectByUsername(registerInfo.getUsername()) != null) {
            log.warn("用户名已存在: {}", registerInfo.getUsername());
            return false;
        }

        // 创建新员工记录
        Emp newEmp = new Emp();
        newEmp.setUsername(registerInfo.getUsername());
        newEmp.setPassword(registerInfo.getPassword()); // 实际应用中应加密存储密码
        newEmp.setPhone(registerInfo.getPhone());

        // 设置默认值
        newEmp.setJob(1); // 默认职位ID
        newEmp.setSalary(5000); // 默认薪资
        newEmp.setDeptId(1); // 默认部门ID
        newEmp.setCreateTime(LocalDateTime.now());
        newEmp.setUpdateTime(LocalDateTime.now());

        // 插入数据库
        empMapper.insert(newEmp);
        return true;
    }
}
