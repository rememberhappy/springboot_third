package com.example.aliclound.dto;

import com.sun.tracing.dtrace.ArgsAttributes;
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
    private String idcardNumber;
    @ApiModelProperty("账户ID")
    private Long accountId;
    @ApiModelProperty("生活照")
    private String image;

    public IdcardDiscernVo(String name, String idcardNumber) {
        this.name = name;
        this.idcardNumber = idcardNumber;
    }
}
