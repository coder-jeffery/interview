package com.easy.interviewsecurity.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * BCrypt 密码哈希，单向，不可解密
 */
public class BCryptUtil {

    /**
     * 生成bcrypt哈希
     * @param password 原始明文密码
     * @param cost 算力 4‑31，数字越大越慢；业务常用10‑12
     * @return bcrypt哈希字符串（内部已经包含salt）
     */
    public static String hashPassword(String password, int cost) {
        // 自动生成随机salt
        String salt = BCrypt.gensalt(cost);
        return BCrypt.hashpw(password, salt);
    }

    /**
     * 校验密码
     * @param rawPassword 用户输入明文
     * @param hashedPassword 数据库存储的bcrypt哈希
     * @return true密码正确
     */
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2")) {
            return false;
        }
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    public static void main(String[] args) {
        String pwd = "MyPass@123456";
        int cost = 12;

        // 生成哈希，每次结果不一样（salt随机）
        String hash = hashPassword(pwd, cost);
        System.out.println("BCrypt哈希值：" + hash);

        // 校验正确密码
        boolean ok1 = checkPassword(pwd, hash);
        System.out.println("校验正确密码：" + ok1);

        // 校验错误密码
        boolean ok2 = checkPassword("WrongPass", hash);
        System.out.println("校验错误密码：" + ok2);
    }
}

