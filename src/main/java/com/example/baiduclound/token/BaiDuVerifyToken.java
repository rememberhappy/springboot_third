package com.example.baiduclound.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common.constants.ALiAiUrlConstants;
import com.example.common.constants.ErrorCodeConstants;
import com.example.common.utils.RedissonTemplate;
import com.example.common.utils.RestTemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 百度AIP开放平台使用OAuth2.0授权调用开放API，调用API时必须在URL中带上access_token参数
 */
@Component
public class BaiDuVerifyToken {
    private static final Logger logger = LoggerFactory.getLogger("BaiDuAccessToken");

    @Value("${ai.baidu.plan_id}")
    private int planId;
    @Autowired
    RedissonTemplate redissonTemplate;
    @Autowired
    RestTemplateUtils restTemplateUtils;
    @Autowired
    BaiDuAccessToken baiDuAccessToken;

    /**
     * 通过access_token获取的verify_token
     *
     * @param
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/8/16 15:49
     */
    public String getVerifyToken(Long accountId) throws Exception {
        // 获取账户信息，账户信息不为空，且账户信息必须是已认证
//        AccountProfile accountProfile = commonUserService.findAccountProfile(accountId);
//        if (accountProfile == null) {
//            throw new RuntimeException(ErrorCodeConstants.ErrorMessage.AI_O5);
//        }
//        if (accountProfile.getIdAuthStatus() == CommonConstants.ID_OAUTH_STATUS &&
//                accountProfile.getFaceAuthStatus() == CommonConstants.FACE_OAUTH_STATUS) {
//            throw new RuntimeException(ErrorCodeConstants.ErrorMessage.AI_12);
//        }
        Map<String, Integer> param = new HashMap<>(1);
        param.put("plan_id", planId);
        String verifyTokenResonse = restTemplateUtils.postForObjectAboutJson(ALiAiUrlConstants.VERIFY_TOKEN_URL.concat(baiDuAccessToken.getAccessToken()), param, String.class);
        JSONObject verifyTokenJb = JSON.parseObject(verifyTokenResonse);
        Object successObj = verifyTokenJb.get("success");
        if (successObj != null) {
            boolean success = (boolean) successObj;
            if (success) {
                String verifyToken = (String) verifyTokenJb.getJSONObject("result").get("verify_token");
                logger.info("获取AI_VERIFY_TOKEN成功, AI_VERIFY_TOKEN:{}", verifyToken);
                return verifyToken;
            }
        }
        logger.error("获取AI_VERIFY_TOKEN异常, message:{}", verifyTokenResonse);
        throw new Exception(ErrorCodeConstants.ErrorMessage.AI_O2);
    }
}
