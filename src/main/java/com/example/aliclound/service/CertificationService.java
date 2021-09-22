package com.example.aliclound.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.FaceVerifyRequest;
import com.example.aliclound.bo.AuthenticationHumanIDDataBo;
import com.example.aliclound.dto.IdcardDiscernAiDto;
import com.example.aliclound.dto.IdcardDiscernDto;
import com.example.aliclound.dto.IdcardDiscernVo;
import com.example.aliclound.enums.IdcardDiscernEnum;
import com.example.aliclound.ossService.OssService;
import com.example.aliclound.token.BaiDuAccessToken;
import com.example.aliclound.token.BaiDuVerifyToken;
import com.example.common.bo.BussinessBo;
import com.example.common.constants.*;
import com.example.common.utils.ImageUtils;
import com.example.common.utils.RedissonTemplate;
import com.example.common.utils.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CertificationService {
    private static final Logger logger = LoggerFactory.getLogger("CertificationService");

    @Value("${ai.baidu.faceverify.threshold}")
    float faceverifyThreshold;
    @Value("${ai.baidu.personverify.threshold}")
    float personVerifyThreshold;
    @Value("${project.env}")
    String bizTypeEnv;
    @Autowired
    RedissonTemplate redissonTemplate;
    @Autowired
    AipFace aipClient;
    @Autowired
    BaiDuAccessToken baiDuAccessToken;
    @Autowired
    BaiDuVerifyToken baiDuVerifyToken;
    @Autowired
    RestTemplateUtils restTemplateUtils;
    @Autowired
    ImageUtils imageUtils;
    @Autowired
    OssService ossService;

    /**
     * 百度云OCR 身份证正反面 信息识别
     * 识别到的姓名和身份证号一般返给页面，供用户自己确认
     *
     * @param dto       身份证正反面
     * @param accountId 实名认证的账户ID
     * @return com.example.common.bo.BussinessBo<com.example.aliclound.dto.IdcardDiscernVo>
     * @Author zhangdj
     * @date 2021/8/15 18:31
     */
    public BussinessBo<IdcardDiscernVo> idcardDiscern(IdcardDiscernDto dto, Long accountId) throws Exception {
        // 1. 根据账户ID查询账户信息
//        AccountProfile accountProfile = commonUserService.findAccountProfile(accountId);
        // 2. 账户信息不存在
//        if (accountProfile == null) {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_O5, ErrorCodeConstants.ErrorMessage.AI_O5);
//        }
        // 账户已认证
//        if (accountProfile.getIdAuthStatus() == MonitorConstants.ID_OAUTH_STATUS &&
//                accountProfile.getFaceAuthStatus() == MonitorConstants.FACE_OAUTH_STATUS) {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_12, ErrorCodeConstants.ErrorMessage.AI_12);
//        }
        AuthenticationHumanIDDataBo humanIdDataBo = new AuthenticationHumanIDDataBo();
        // 身份证反面验证
        humanIdDataBo = idcardDiscernFromBaidu(accountId, dto.getBack(), IdcardDiscernEnum.BACK_TYPE.getName(), humanIdDataBo);
        // 身份证正面验证
        humanIdDataBo = idcardDiscernFromBaidu(accountId, dto.getFront(), IdcardDiscernEnum.FRONT_TYPE.getName(), humanIdDataBo);
        // 将实名认证后的信息保存到本地
//        Resp<Boolean> saveResponse = userService.updateAccountProfile(accountProfile, humanIdDataBo);
//        if (saveResponse.isSuccess() && saveResponse.getData()) {
//            // 保存成功，返回 姓名和身份证号
        return BussinessBo.success(new IdcardDiscernVo(humanIdDataBo.getName(), humanIdDataBo.getIdcardNumber()));
//        } else {
//            // 保存失败，返回错误信息
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_O7, ErrorCodeConstants.ErrorMessage.AI_O7);
//        }
    }

    /**
     * 身份证验证，从百度OCR识别
     */
    private AuthenticationHumanIDDataBo idcardDiscernFromBaidu(Long accountId, String image, String type, AuthenticationHumanIDDataBo humanIdDataBo) throws Exception {
        IdcardDiscernAiDto idCardDto = new IdcardDiscernAiDto(imageUtils.imageBase64Compress(image), type);
        Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(idCardDto), HashMap.class);
        // 阿里ocr身份证识别
        String discernResp = restTemplateUtils.postForObjectAboutUrlencoded(ALiAiUrlConstants.OAUTH_ID_CARD_ISCERN_URL.concat(baiDuAccessToken.getAccessToken()), map, String.class);
        JSONObject jsonObject = JSONObject.parseObject(discernResp);
        // 根据ocr身份证识别返回信息，判断是否识别成功
        BussinessBo checkBo = AIIdCardDiscernErrorInfoConstants.checkImageStatus(jsonObject.getString("image_status"));
        logger.info("account:[{}]-[{}]身份识别信息:[{}]", accountId, type, discernResp);
        if (!checkBo.status()) {
            throw new RuntimeException(checkBo.getMessage());
        }
        if (jsonObject.get("idcard_number_type") != null) {
            checkBo = AIIdCardDiscernErrorInfoConstants.checkIdNumType(jsonObject.getInteger("idcard_number_type"));
            if (!checkBo.status()) {
                throw new RuntimeException(checkBo.getMessage());
            }
        }
        int wordResultNum = jsonObject.getInteger("words_result_num");
        if (wordResultNum == 0) {
            // 身份识别异常
            throw new RuntimeException(ErrorCodeConstants.ErrorMessage.AI_13);
        }
        // 身份证识别成功后，身份证图片信息的获取
        return getIdCardData(jsonObject.getJSONObject("words_result"), humanIdDataBo);
    }

    /**
     * 获取阿里云 OCR识别身份证后返回的信息
     */
    private AuthenticationHumanIDDataBo getIdCardData(JSONObject wordResults, AuthenticationHumanIDDataBo bo) {

        JSONObject expireTimeJb = wordResults.getJSONObject("失效日期");
        if (expireTimeJb != null) {
            bo.setExpireTime(expireTimeJb.getString("words"));
        }
        JSONObject issueTimeJb = wordResults.getJSONObject("签发日期");
        if (issueTimeJb != null) {
            bo.setIssueTime(issueTimeJb.getString("words"));
        }
        JSONObject issueAuthorityJb = wordResults.getJSONObject("签发机关");
        if (issueAuthorityJb != null) {
            bo.setIssueAuthority(issueAuthorityJb.getString("words"));
        }
        JSONObject nameJb = wordResults.getJSONObject("姓名");
        if (nameJb != null) {
            bo.setName(nameJb.getString("words"));
        }
        JSONObject nationJb = wordResults.getJSONObject("民族");
        if (nationJb != null) {
            bo.setNation(nationJb.getString("words"));
        }
        JSONObject addressJb = wordResults.getJSONObject("住址");
        if (addressJb != null) {
            bo.setAddress(addressJb.getString("words"));
        }
        JSONObject idCardNoJb = wordResults.getJSONObject("公民身份号码");
        if (idCardNoJb != null) {
            bo.setIdcardNumber(idCardNoJb.getString("words"));
        }
        JSONObject birthDayJb = wordResults.getJSONObject("出生");
        if (birthDayJb != null) {
            bo.setBirthday(birthDayJb.getString("words"));
        }
        JSONObject genderJb = wordResults.getJSONObject("性别");
        if (genderJb != null) {
            // 汉字：男，女
            bo.setGender("男".equals(genderJb.getString("words")) ? 1 : 2);
        }
        return bo;
    }

    /**
     * 人脸实名认证
     *
     * @param idcardDiscernVo 用来接收图片地址，账户ID，姓名和身份证号
     * @return com.example.common.bo.BussinessBo<java.lang.Boolean>
     * @Throws
     * @Author zhangdj
     * @date 2021/8/17 11:25
     */
    public BussinessBo<Boolean> faceverify(IdcardDiscernVo idcardDiscernVo) throws Exception {
        // 查询实名认证信息，判断实名认证信息是否存在，判断用户名和身份证号是否为空，判断该账户是否已认证
//        AccountProfile accountProfile = commonUserService.findAccountProfile(accountId);
//        if (accountProfile == null) {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_O5, ErrorCodeConstants.ErrorMessage.AI_O5);
//        }
//        if (StringUtils.isBlank(accountProfile.getName()) || StringUtils.isBlank(accountProfile.getIdCardNo())) {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_14, ErrorCodeConstants.ErrorMessage.AI_14);
//        }
//        if (accountProfile.getIdAuthStatus() == MonitorConstants.ID_OAUTH_STATUS &&
//                accountProfile.getFaceAuthStatus() == MonitorConstants.FACE_OAUTH_STATUS) {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_12, ErrorCodeConstants.ErrorMessage.AI_12);
//        }
        String userPhotoBase64 = imageUtils.imageBase64Compress(idcardDiscernVo.getImage());
        FaceVerifyRequest req = new FaceVerifyRequest(userPhotoBase64, CommonConstants.BASE64);
        List<FaceVerifyRequest> list = new ArrayList<>();
        list.add(req);
        // 活体检测
        org.json.JSONObject res = aipClient.faceverify(list);

        logger.info("活体检测,msg:[{}]", res.toString());
        if (res.has("error_code")) {
            int errorCode = (int) res.get("error_code");
            // String errorMsg = (String) res.get("error_msg");
            if (errorCode != 0) {
                logger.error("活体检测异常,msg:[{}]", res.toString());
                String codeMsg = AIErrorCodeMessageConstants.getCodeMessage(errorCode);
                return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_15, codeMsg);
            }
        }
        double faceLiveness = (double) res.getJSONObject("result").get("face_liveness");
        if (faceLiveness < faceverifyThreshold) {
            return BussinessBo.success(false);
        }
        // 公安验证
        BussinessBo<Boolean> personVerifyBo = personVerify(idcardDiscernVo.getImage(), CommonConstants.BASE64,
                idcardDiscernVo.getIdCardNumber(), idcardDiscernVo.getName());
        if (personVerifyBo.status()) {
            // 身份信息公安验证成功，保存人脸认证结果
            AuthenticationHumanIDDataBo humanIDData = new AuthenticationHumanIDDataBo();
            humanIDData.setFaceAuthTime(new Date());
            humanIDData.setIdAuthTime(new Date());
            humanIDData.setFaceAuthStatus(CommonConstants.FACE_OAUTH_STATUS);
            humanIDData.setIdAuthStatus(CommonConstants.ID_OAUTH_STATUS);
            humanIDData.setUserPhoto(ossService.uploadFileOnEncrypt(idcardDiscernVo.getAccountId(), ossService.dataEncrypt(userPhotoBase64)));
            // 保存人脸认证后的信息
//            Resp<Boolean> resp = userService.updateAccountProfile(accountProfile, humanIDData);
//            if (resp.isSuccess() && resp.getData()) {
            return BussinessBo.success(true);
//            } else {
//                // 保存认证结果失败
//                return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_O7, ErrorCodeConstants.ErrorMessage.AI_O7);
//            }
        } else {
            // 身份信息公安验证失败
            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_16, personVerifyBo.getMessage());
        }
    }

    /**
     * 公安验证
     */
    private BussinessBo<Boolean> personVerify(String image, String imageType, String idCardNumber, String name) throws JSONException {
        // https://ai.baidu.com/ai-doc/FACE/8k37c1rqz#%E8%BA%AB%E4%BB%BD%E9%AA%8C%E8%AF%81
        // 质量检测（可选）活体检测（可选）公安验证（必选）
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", "NONE");
        options.put("liveness_control", "NONE");
        // 公安验证
        org.json.JSONObject res = aipClient.personVerify(image, imageType, idCardNumber, name, options);
        if (res.has("error_code")) {
            int errorCode = res.getInt("error_code");
            // String errorMsg = (String) res.get("error_msg");
            if (errorCode != 0) {
                logger.error("公安检测异常,msg:[{}]", res.toString());
                String codeMsg = AIErrorCodeMessageConstants.getCodeMessage(errorCode);
                return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_16, codeMsg);
            }
        }
        logger.info("公安鉴定,msg:[{}]", res.toString());
        double score = res.getJSONObject("result").getDouble("score");
        if (score >= personVerifyThreshold) {
            return BussinessBo.success(true);
        } else {
            return BussinessBo.error();
        }
    }

    /**
     * AI实名认证成功后, 维护用户认证身份信息, 以及用户的人脸库信息
     * 此操作必须在人脸实名认证后
     *
     * @param accountId
     * @param verifyToken
     * @return void
     * @Throws
     * @Author zhangdj
     * @date 2021/8/17 13:57
     */
    public void updateHumanFaceAndAuthenIDData(Long accountId, String verifyToken) throws Exception {
        // 查询账户信息，该账户必须是认证后的
//        AccountProfile accountProfile = commonUserService.findAccountProfile(CommonInfoHolder.getAccountId());
//        if (accountProfile == null) {
//            throw new Exception(ErrorCodeConstants.ErrorMessage.AI_O5);
//        }
//        if (accountProfile.getIdAuthStatus() == 1 && accountProfile.getFaceAuthStatus() == 1 &&
//                StringUtils.isNotBlank(accountProfile.getUserPhoto())) {
//            return;
//        }
        // 根据认证token【verifyToken】获取认证数据
        AuthenticationHumanIDDataBo humanIDData = getAuthenticationResult(verifyToken);
        if (humanIDData == null) {
            // 获取认证结果异常
            throw new Exception(ErrorCodeConstants.ErrorMessage.AI_O4);
        }
        String baiduHumanfaceUrl = getOauthHumanFace(verifyToken);
//        humanIDData.setFaceImageUrl(ossService.uploadFile(accountId, imageUtils.imageByUrl(baiduHumanfaceUrl)));
//        Resp<Boolean> saveResponse = userService.updateAccountProfile(accountProfile, humanIdDataBo);
//        if (saveResp.isSuccess() && saveResp.getData()) {
//            return;
//        }
        throw new Exception(ErrorCodeConstants.ErrorMessage.AI_O7);
    }

    /**
     * 获取认证人脸接口
     * 此接口调用时机: 用户认证成功后才能调用
     */
    private String getOauthHumanFace(String verifyToken) throws Exception {
        String sendOauthHumanFaceUrl = ALiAiUrlConstants.OAUTH_HUMAN_FACE_URL.concat(baiDuAccessToken.getAccessToken());
        Map<String, String> param = new HashMap<>();
        param.put("verify_token", verifyToken);
        String aiBaiduResponseStr = restTemplateUtils.postForObjectAboutJson(sendOauthHumanFaceUrl, param, String.class);
        JSONObject aiBaiduResponseJb = JSON.parseObject(aiBaiduResponseStr);
        Object successObj = aiBaiduResponseJb.get("success");
        if (successObj != null) {
            boolean success = (boolean) successObj;
            if (success) {
                return (String) aiBaiduResponseJb.getJSONObject("result").get("image");
            }
        }
        logger.error("人员实名认证后获取认证人脸, 获取失败:{}", aiBaiduResponseStr);
        throw new Exception(ErrorCodeConstants.ErrorMessage.AI_O3);
    }

    /**
     * 通过verifyToken 获取认证结果
     */
    private AuthenticationHumanIDDataBo getAuthenticationResult(String verifyToken) throws Exception {
        String sendOauthResultDetailUrl = ALiAiUrlConstants.OAUTH_RESULT_DETAIL_URL.concat(baiDuAccessToken.getAccessToken());
        Map<String, String> param = new HashMap<>();
        param.put("verify_token", verifyToken);
        String aiBaiduResponseStr = restTemplateUtils.postForObjectAboutJson(sendOauthResultDetailUrl, param, String.class);
        JSONObject aiBaiduResponseJb = JSON.parseObject(aiBaiduResponseStr);
        Object successObj = aiBaiduResponseJb.get("success");
        if (successObj != null) {
            if ((boolean) successObj) {
                AuthenticationHumanIDDataBo authenticationHumanIDDataBo = new AuthenticationHumanIDDataBo();
                JSONObject resultJb = aiBaiduResponseJb.getJSONObject("result");
                JSONObject idcardOcrResultJb = resultJb.getJSONObject("idcard_ocr_result");
                authenticationHumanIDDataBo.setGender("男".equalsIgnoreCase(idcardOcrResultJb.getString("gender")) ? CommonConstants.MALE :
                        CommonConstants.FEMALE);
                authenticationHumanIDDataBo.setNation(idcardOcrResultJb.getString("nation"));
                authenticationHumanIDDataBo.setExpireTime(idcardOcrResultJb.getString("expire_time"));
                authenticationHumanIDDataBo.setIssueAuthority(idcardOcrResultJb.getString("issue_authority"));
                authenticationHumanIDDataBo.setIssueAuthority(idcardOcrResultJb.getString("issue_time"));
                authenticationHumanIDDataBo.setName(idcardOcrResultJb.getString("name"));
                authenticationHumanIDDataBo.setIdcardNumber(idcardOcrResultJb.getString("id_card_number"));
                authenticationHumanIDDataBo.setAddress(idcardOcrResultJb.getString("address"));
                authenticationHumanIDDataBo.setBirthday(idcardOcrResultJb.getString("birthday"));
                authenticationHumanIDDataBo.setLivenessScore(idcardOcrResultJb.getString("liveness_score"));
                authenticationHumanIDDataBo.setScore(idcardOcrResultJb.getString("score"));
                authenticationHumanIDDataBo.setSpoofing(idcardOcrResultJb.getString("spoofing"));
                authenticationHumanIDDataBo.setFaceAuthStatus(CommonConstants.FACE_OAUTH_STATUS);
                authenticationHumanIDDataBo.setIdAuthStatus(CommonConstants.ID_OAUTH_STATUS);
                authenticationHumanIDDataBo.setIdAuthTime(new Date());
                authenticationHumanIDDataBo.setFaceAuthTime(new Date());
                return authenticationHumanIDDataBo;
            }
        }
        logger.error("人员实名认证后获取认证人脸, 获取对比结果失败:{}", aiBaiduResponseStr);
        return null;
    }

    /**
     * 获取认证后的人脸图片
     *
     * @param accountId 账户ID
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/8/18 10:31
     */
    public String getFace(Long accountId) throws Exception {
        // 人脸照片 OSS 地址
        String userPhotoOSSPath = "https://raas-image-tes.oss-cn-beijing.aliyuncs.com/hrss/userPhoto/20210619/13162410582938034031a44cf7254408a8f6cdd4f9fde8c96.encry .";
        // 获取账户信息
//        AccountProfile accountProfile = commonUserService.findAccountProfile(accountId);
//        if (accountProfile == null) {
//            throw new RuntimeException("查询账户信息异常");
//        }
//        userPhotoOSSPath = accountProfile.getUserFaceCryptographicUrl();
        // 将人脸信息存入到 redis 中，有效期为30分钟
        String humanFaceKey = "M_FACE_A_" + accountId;
        String humanFace = redissonTemplate.getRBucketValue(humanFaceKey);
        if (StringUtils.isBlank(humanFace)) {
            humanFace = ossService.dataDecrypt(ossService.getFileContentFromOSS(userPhotoOSSPath));
            redissonTemplate.setBucketValueAndExpire(humanFaceKey, humanFace, 30, TimeUnit.MINUTES);
        }
        return humanFace;
    }

//    // tokenType => 1: 认证; 2: 活体
//    public BussinessBo verifyResult(Integer tokenType, Long accountId) throws Exception {
////        AccountProfile accountProfile = commonUserService.findAccountProfile(accountId);
////        if (accountProfile == null) {
////            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_O5, ErrorCodeConstants.ErrorMessage.AI_O5);
////        }
//        String bizId = bizTypeEnv.concat(accountId.toString());
//        if (tokenType == 1) {
////            if (CommonConstants.ID_OAUTH_STATUS == accountProfile.getIdAuthStatus() && CommonConstants.FACE_OAUTH_STATUS == accountProfile.getFaceAuthStatus() &&
////                    StringUtils.isNotBlank(accountProfile.getUserPhoto())) {
////                // 该账户已认证
////                return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_12, ErrorCodeConstants.ErrorMessage.AI_12);
////            }
//        } else {
//            bizId = redissonTemplate.getRBucketValue(RedisKeyConstants.ALI_HUMAN_FACE_COMPARE_KEY.concat(bizId));
//        }
//        DescribeVerifyResultResponse verifyResult = verifyResultService.getVerifyResult(tokenType, bizId);
//        ULogger.info("ali认证信息:[{}]", JSON.toJSONString(verifyResult));
//        int verifyStatus = verifyResult.getVerifyStatus();
//        if (verifyStatus == -1) {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_10, ErrorCodeConstants.ErrorMessage.AI_10);
//        } else if (verifyStatus != 1) {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_11, ErrorCodeConstants.ErrorMessage.AI_11.concat(
//                    "(" + AliVerifyStatusMap.getCacheStatus(verifyStatus) + ")"
//            ));
//        }
//        if (tokenType == 2) {
//            return BussinessBo.success();
//        }
//        BussinessBo<AuthenticationHumanIDDataBo> authenticationHumanInfoBo = verifyResultService.humanIDData(verifyResult, accountId);
//        if (!authenticationHumanInfoBo.status()) {
//            return authenticationHumanInfoBo;
//        }
//        Resp<Boolean> saveResponse = userService.updateAccountProfile(accountProfile, authenticationHumanInfoBo.getData());
//        if (saveResponse.isSuccess() && saveResponse.getData()) {
//            return BussinessBo.success();
//        } else {
//            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_O7, ErrorCodeConstants.ErrorMessage.AI_O7);
//        }
//    }
}
