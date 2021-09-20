package com.example.aliclound.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 认证后用户身份信息
 */
@ApiModel("认证后用户身份信息")
@Setter
@Getter
public class AuthenticationHumanIDDataBo {
    @ApiModelProperty("用户照片")
    private String userPhoto;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("身份证号")
    private String idcardNumber;
    @ApiModelProperty("性别")
    private Integer gender;
    @ApiModelProperty("民族")
    private String nation;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("生日")
    private String birthday;
    @ApiModelProperty("身份证生效日期")
    private String issueTime;
    @ApiModelProperty("身份证失效日期")
    private String expireTime;
    @ApiModelProperty("身份证签发机关")
    private String issueAuthority;
    @ApiModelProperty("活体检测分数")
    private String livenessScore;
    @ApiModelProperty("公安验证")
    private String score;
    /*
        合成图分数
        若未进行合成图检测，则返回0
        若进行活体检测，则返回合成图检测分值
     */
    @ApiModelProperty("合成图分数")
    private String spoofing;
    @ApiModelProperty("用户认证状态")
    private Integer idAuthStatus;
    @ApiModelProperty("人脸认证状态")
    private Integer faceAuthStatus;
    @ApiModelProperty("用户认证时间")
    private Date idAuthTime;
    @ApiModelProperty("人脸认证时间")
    private Date faceAuthTime;
}
