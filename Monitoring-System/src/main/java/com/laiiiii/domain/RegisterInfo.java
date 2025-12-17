package com.laiiiii.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装注册结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInfo {
    private String username; // 用户名
    private String password; // 密码
    private String phone; // 手机号
}
