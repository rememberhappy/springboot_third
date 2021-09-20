package com.example.common.utils;

import org.springframework.util.DigestUtils;

public class EncryptUtils {

    public static String MD5(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes());
    }
}
