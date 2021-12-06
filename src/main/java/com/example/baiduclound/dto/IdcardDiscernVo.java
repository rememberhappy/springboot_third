package com.example.baiduclound.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IdcardDiscernVo {

    @ApiModelProperty("name")
    private String name;
    @ApiModelProperty("身份证号")
    private String idCardNumber;
    @ApiModelProperty("账户ID")
    private Long accountId;
    @ApiModelProperty("生活照")
    private String image;

    public IdcardDiscernVo(String name, String idCardNumber) {
        this.name = name;
        this.idCardNumber = idCardNumber;
    }
}
