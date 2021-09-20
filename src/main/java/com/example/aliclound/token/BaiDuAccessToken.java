package com.example.aliclound.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common.constants.ALiAiUrlConstants;
import com.example.common.constants.ErrorCodeConstants;
import com.example.common.constants.RedisKeyConstants;
import com.example.common.utils.RedissonTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 百度AIP开放平台使用OAuth2.0授权调用开放API，调用API时必须在URL中带上access_token参数
 */
@Component
public class BaiDuAccessToken {
    private static final Logger logger = LoggerFactory.getLogger("BaiDuAccessToken");

    @Value("${ai.baidu.client_id}")
    private String clientId; // 应用的API Key
    @Value("${ai.baidu.client_secret}")
    private String clientSecret; // 应用的Secret Key
    @Autowired
    RedissonTemplate redissonTemplate;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取百度 access_token
     *
     * @param
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/8/16 15:49
     */
    public String getAccessToken(){
        String accessToken = redissonTemplate.getRBucketValue(RedisKeyConstants.AI_ACCESS_TOKEN_KEY);
        if (StringUtils.isBlank(accessToken)) {

            String sendAiBaiduTokenUrl = ALiAiUrlConstants.ACCESS_TOKEN_URL.concat("?grant_type=client_credentials&client_id=")
                    .concat(clientId).concat("&client_secret=").concat(clientSecret);
            String tokenResponse = restTemplate.getForObject(sendAiBaiduTokenUrl, String.class);
            JSONObject tokenResponseJb = JSON.parseObject(tokenResponse);
            Object errorCodeObj = tokenResponseJb.get("error");
            if (errorCodeObj == null) {
                accessToken = (String) tokenResponseJb.get("access_token");
                int expiresIn = (int) tokenResponseJb.get("expires_in");
                redissonTemplate.setBucketValueAndExpire(RedisKeyConstants.AI_ACCESS_TOKEN_KEY, accessToken, expiresIn - 100, TimeUnit.SECONDS);
                logger.info("获取AI_ACCESS_TOKEN_KEY成功, AI_ACCESS_TOKEN_KEY:{}, expiresIn:{}",
                        accessToken, expiresIn);
            } else {
                logger.error("获取AI_ACCESS_TOKEN_KEY失败, message:{}", tokenResponse);
            }
            if (StringUtils.isNotBlank(accessToken)) {
                return accessToken;
            }
        } else {
            return accessToken;
        }
        throw new RuntimeException(ErrorCodeConstants.ErrorMessage.AI_O1);
    }
}
