package com.example.baiduclound.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 阿里ocr身份证识别需要的参数key，封装成对象，用的时候，对象转map.
 */
@Setter
@Getter
public class IdcardDiscernAiDto {

    private String image;
    // 取值字段 IdcardDiscernEnum：front/back	-front：身份证含照片的一面 -back：身份证带国徽的一面
    private String id_card_side;
    // true/false	是否检测头像内容，默认不检测。可选值：true-检测头像并返回头像的 base64 编码及位置信息
    private boolean detect_photo;

    public IdcardDiscernAiDto(String image, String idCardSide) {
        this(image, idCardSide, false);
    }

    public IdcardDiscernAiDto(String image, String id_card_side, boolean detect_photo) {
        this.image = image;
        this.id_card_side = id_card_side;
        this.detect_photo = detect_photo;
    }
}
