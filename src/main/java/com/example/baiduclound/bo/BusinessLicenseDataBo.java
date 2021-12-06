package com.example.baiduclound.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 营业执照 ocr识别信息
 */
@Setter
@Getter
public class BusinessLicenseDataBo {

    @ApiModelProperty("社会信用代码")
    private String socialCreditCode;
    @ApiModelProperty("组成形式")
    private String form;
    @ApiModelProperty("经营范围")
    private String scopeBusiness;
    @ApiModelProperty("成立日期")
    private String registerDate;
    @ApiModelProperty("法人")
    private String asLegalPerson;
    @ApiModelProperty("注册资本")
    private String registeredCapital;
    @ApiModelProperty("证件编号")
    private String idNumber;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("单位名称")
    private String nameTheEntity;
    @ApiModelProperty("有效期")
    private String periodValidity;
    @ApiModelProperty("类型")
    private String type;
}
