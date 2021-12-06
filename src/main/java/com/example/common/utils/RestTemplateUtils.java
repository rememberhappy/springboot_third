package com.example.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RestTemplateUtils {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用 restTemplate 发送请求
     *
     * @param url   请求地址
     * @param obj   请求参数
     * @param clazz 返回的数据类型
     * @return T
     * @Author zhangdj
     * @date 2021/12/6 16:04
     */
    public <T> T postForObjectAboutJson(String url, Object obj, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(obj), httpHeaders);
        return restTemplate.postForObject(url, httpEntity, clazz);
    }

    public <T> T postForObjectAboutUrlencoded(String url, Map<String, Object> obj, Class<T> clazz) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(getParams(obj), headers);
        return restTemplate.postForObject(url, httpEntity, clazz);
    }

    private MultiValueMap<String, Object> getParams(Map<String, Object> obj) {
        MultiValueMap<String, Object> returnMap = new LinkedMultiValueMap<>(obj.size());
        for (Map.Entry<String, Object> entrySet : obj.entrySet()) {
            returnMap.add(entrySet.getKey(), entrySet.getValue());
        }
        return returnMap;
    }

}
