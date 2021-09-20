package com.example.aliclound.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 身份证正反面
 */
@Setter
@Getter
public class IdcardDiscernDto {
    @ApiModelProperty("身份证正面图片base64编码(要求压缩,base64编码和urlencode后大小不超过4M，最短边至少15px，最长边最大4096px,支持jpg/jpeg/png/bmp格式)")
    private String front;
    @ApiModelProperty("身份证反面图片base64编码")
    private String back;
}