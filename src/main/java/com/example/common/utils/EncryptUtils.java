package com.example.common.utils;

import org.springframework.util.DigestUtils;

/**
 * 使用spring-core中自带的工具类进行MD5加密
 */
public class EncryptUtils {

    public static String MD5(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes());
    }
}
