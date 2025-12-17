package com.laiiiii.util;

import java.util.regex.Pattern;

public class PasswordValidator {
    /**
     * 验证密码复杂度
     * 至少8个字符，包含字母和数字
     *
     * @param password 待验证的密码
     * @return 验证结果，true表示符合要求，false表示不符合要求
     */
    public static boolean isValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // 检查是否包含字母
        boolean hasLetter = Pattern.compile("[a-zA-Z]").matcher(password).find();
        // 检查是否包含数字
        boolean hasDigit = Pattern.compile("[0-9]").matcher(password).find();

        return hasLetter && hasDigit;
    }
}
