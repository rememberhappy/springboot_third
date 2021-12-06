package com.example.baiduclound.enums;

import lombok.Getter;

/**
 * 身份证正反枚举
 */
@Getter
public enum IdcardDiscernEnum {
    // 身份证正面图片base64编码(要求压缩,base64编码和urlencode后大小不超过4M，最短边至少15px，最长边最大4096px,支持jpg/jpeg/png/bmp格式)
    // 正面是有头像的一面，反面是有国徽的一面
    FRONT_TYPE("正面，有头像的一面", "front"), BACK_TYPE("反面，有国徽的一面", "back");

    private final String remarks;
    private final String name;

    IdcardDiscernEnum(String remarks, String name) {
        this.remarks = remarks;
        this.name = name;
    }
}
