package com.example.common.config;

import com.baidu.aip.face.AipFace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaiduConfiguration {

    @Value("${ai.baidu.client_id}")
    private String clientId; // 应用的API Key
    @Value("${ai.baidu.client_secret}")
    private String clientSecret; // 应用的Secret Key
    // 方案的id信息，请在人脸实名认证控制台查看创建的H5方案的方案ID信息
    @Value("${ai.baidu.plan_id}")
    private int planId;

    @Bean
    public AipFace aipFace() {
        // 初始化一个AipFace
        AipFace client = new AipFace(String.valueOf(planId), clientId, clientSecret);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }
}
