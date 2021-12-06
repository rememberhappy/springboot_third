package com.example.baiduclound.collection;

import com.example.baiduclound.bo.BusinessLicenseDataBo;
import com.example.baiduclound.dto.IdcardDiscernDto;
import com.example.baiduclound.dto.IdcardDiscernVo;
import com.example.baiduclound.service.CertificationService;
import com.example.common.bo.BussinessBo;
import com.example.common.constants.ErrorCodeConstants;
import com.google.common.base.Preconditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author zhangdj
 * @Date 2021/8/15:17:37
 */
@Api(tags = "阿里云第三方接口")
@RestController
public class CertificationCollection {
    @Autowired
    CertificationService certificationService;

    @ApiOperation("ocr 身份证信息识别，返回姓名和身份证号供用户进行确认")
    @PostMapping("idcardDiscern")
    public BussinessBo<IdcardDiscernVo> idcardDiscern(@RequestBody IdcardDiscernDto dto, Long accountId) throws Exception {
        return certificationService.idcardDiscern(dto, accountId);
    }

    @ApiOperation("获取营业执照信息")
    @PostMapping("getBusinessLicense")
    public BusinessLicenseDataBo getBusinessLicense(@RequestBody String image) {
        Preconditions.checkArgument(StringUtils.isNotBlank(image), "营业执照信息不能为空");
        // 营业执照ocr识别
        return certificationService.businessLicenseFromBaidu(image);
    }

    /**
     * 1. 质量检测（可选）：判断图片中是否包含人脸，以及人脸在姿态、遮挡、模糊、光照等方面是否符合识别条件；
     * 2. 活体检测（可选）：基于图片中的破绽分析，判断其中的人脸是否为二次翻拍
     * （举例：如用户A用手机拍摄了一张包含人脸的图片一，用户B翻拍了图片一得到了图片二，并用图片二伪造成用户A去进行识别操作，这种情况普遍发生在金融开户、实名认证等环节。）
     * 3. 人脸实名认证（必选）：基于姓名和身份证号，调取公安权威数据源人脸图，将当前获取的人脸图片，与此公安数据源人脸图进行对比，得出比对分数，并基于此进行业务判断是否为同一人。
     * 由于公安数据源人脸图具有最权威的身份证明作用，故对用户本人的验证结果可信度也最为合理。
     *
     * @param idcardDiscernVo 用来接收图片地址，账户ID，姓名和身份证号
     * @return com.example.common.bo.BussinessBo<java.lang.String>
     * @Author zhangdj
     * @date 2021/8/16 18:59
     */
    @ApiOperation("人脸实名认证")
    @PostMapping("faceverify")
    public BussinessBo<Boolean> faceverify(@RequestBody IdcardDiscernVo idcardDiscernVo) throws Exception {
        BussinessBo<Boolean> bo = certificationService.faceverify(idcardDiscernVo);
        if (bo.status() && bo.getData()) {
            return BussinessBo.success(bo.getData());
        } else {
            if (!bo.status()) {
                return BussinessBo.error(bo.getCode(), bo.getMessage());
            } else {
                return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_15, "活体检测未通过");
            }
        }
    }

    @ApiOperation("是否实名认证，验证当前用户是否经过【ocr 身份证信息识别，人脸实名认证】步骤")
    @PostMapping("isRealNameAuthentication")
    public BussinessBo<Boolean> isRealNameAuthentication(Long accountId) {
        // 实名认证信息已经存在本地了，查询本地数据库中对应账号的 身份认证状态和人脸认证状态
        return BussinessBo.success(true);
    }

    // 这个有问题
    @ApiOperation("AI实名认证成功后, 更新用户认证身份信息【通过verifyToken获取用户认证信息】, 以及用户的人脸库信息")
    @PostMapping("realNameAuthenticationSuccess")
    public BussinessBo<Boolean> realNameAuthenticationAfterUpdateUserDataAndImage(Long accountId, String verifyToken) throws Exception {
        certificationService.updateHumanFaceAndAuthenIDData(accountId, verifyToken);
        return BussinessBo.success(true);
    }

//    // tokenType == 1: 认证; 2: 活体  获取认证结果, 以及保存认证信息
//    @ApiOperation("ali:获取认证结果,维护认证信息")
//    @GetMapping("verifyResult")
//    public BussinessBo<Boolean> verifyResult(@RequestParam Integer tokenType, Long accountId) throws Exception {
//        BussinessBo bussinessBo = certificationService.verifyResult(tokenType, accountId);
//        if (bussinessBo.status()) {
//            return BussinessBo.success(true);
//        } else {
//            return BussinessBo.error(bussinessBo.getCode(), bussinessBo.getMessage());
//        }
//    }

    @ApiModelProperty("获取用户人脸(base64数据(无图片类型标识))")
    @GetMapping("getFece")
    public BussinessBo<String> getFace(Long accountId) throws Exception {
        // 本地数据中存放人脸照片的oss地址，通过oss地址获取照片数据
        return BussinessBo.success(certificationService.getFace(accountId));
    }

    @ApiOperation("人脸对比")
    @PostMapping("comparison")
    public Boolean humanFaceComparison(@RequestBody String image) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(image), "人脸图片不能为空");
        return certificationService.humanFaceComparison(image);
    }
}