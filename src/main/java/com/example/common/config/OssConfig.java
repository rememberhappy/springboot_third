package com.example.common.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;


    /*@Value("${aliyun.doc.fileHost}")
    private String fileHost;
    @Value("${aliyun.doc.fileOutPut}")
    private String docFileOutPut;
    @Value("${aliyun.doc.projectName}")
    private String projectName;
    @Value("${aliyun.doc.region}")
    private String region;
    @Value("${aliyun.visitScoPrefix}")
    private String visitScoPrefix;
    @Value("${aliyun.doc.docFileHost}")
    private String docFileHost;*/

    @Bean(name = "ossClient")
    public OSS ossClient(){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }

    /*public String getFileHost() {
        return fileHost;
    }

    public void setFileHost(String fileHost) {
        this.fileHost = fileHost;
    }

    public String getDocFileHost() {
        return docFileHost;
    }

    public void setDocFileHost(String docFileHost) {
        this.docFileHost = docFileHost;
    }

    public String getDocFileOutPut() {
        return docFileOutPut;
    }

    public void setDocFileOutPut(String docFileOutPut) {
        this.docFileOutPut = docFileOutPut;
    }

    public String getVisitScoPrefix() {
        return visitScoPrefix;
    }

    public void setVisitScoPrefix(String visitScoPrefix) {
        this.visitScoPrefix = visitScoPrefix;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }*/
    public String getAccessKeyId() {
        return accessKeyId;
    }
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }
    public String getAccessKeySecret() {
        return accessKeySecret;
    }
    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
