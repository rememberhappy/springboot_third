package com.example.aliclound.ossService;

import com.aliyun.oss.OSS;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.SSEAlgorithm;
import com.example.common.constants.CommonConstants;
import com.example.common.utils.AESUtil;
import com.example.common.utils.DateUtils;
import com.example.common.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阿里云 OSS【文件服务器】 照片上传下载
 */
@Service
public class OssService {
    private static final Logger logger = LoggerFactory.getLogger("OssService");

    @Value("${aliyun.oss.endpoint}")
    String endpoint;
    @Value("${aliyun.oss.bucket}")
    String bucket;
    @Value("${aliyun.oss.filePathPrefix}")
    String filePathPrefix;
    @Autowired
    ImageUtils iamgeUtil;
    @Qualifier("ossClient")
    @Autowired
    OSS ossClient;

    private AtomicInteger count = new AtomicInteger(0);
    private Lock lock = new ReentrantLock();
    private static final int COUNT_LIMIT = 10000;
    private static String bucketName = "images";

//    public String uploadFile(Long accountId, byte[] datas) {
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        String fullPath = filePathPrefix.concat("userIdPhoto").concat("/")
//                .concat(DateUtils.getDate2String(new Date()))
//                .concat("/").concat(String.valueOf(accountId))
//                .concat(String.valueOf(System.currentTimeMillis()))
//                .concat(String.valueOf(getCount())).concat(uuid).concat(".jpg");
//        ossClient.putObject(bucket, fullPath, new ByteArrayInputStream(datas));
//        return String.format("https://%s.%s/%s", bucket, endpoint, fullPath);
//    }

    public String getFullPathFromDB(String userPhotoPath) {
        int index = userPhotoPath.indexOf(filePathPrefix);
        return userPhotoPath.substring(index);
    }

    // 数据加密
    public byte[] dataEncrypt(String data, String sKey, String ivParameterKey) throws Exception {
        logger.info("------- 数据加密开始");
        String compressData = iamgeUtil.imageBase64Compress(data);
        return AESUtil.encrypt(compressData.getBytes("utf-8"), sKey, ivParameterKey).getBytes();
    }

    // 数据加密
    public byte[] dataEncrypt(String data) throws Exception {
        logger.info("------- 数据加密开始");
        String compressData = iamgeUtil.imageBase64Compress(data);
        return AESUtil.encrypt(compressData.getBytes("utf-8")).getBytes();
    }

    // 数据解密
    public String dataDecrypt(String data) throws Exception {
        byte[] decrypt = AESUtil.decrypt(data);
        return new String(decrypt, "utf-8");
    }

    /**
     * 文件上传
     *
     * @param pathDir
     * @param id
     * @param datas
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/8/16 16:28
     */
    public String uploadFileForCapture(String pathDir, Long id, byte[] datas) {
        logger.info("------- 数据上传开始");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fullPath = filePathPrefix.concat(pathDir).concat("/")
                .concat(DateUtils.getDate2String(new Date()))
                .concat("/").concat(String.valueOf(id))
                .concat(String.valueOf(System.currentTimeMillis()))
                .concat(String.valueOf(getCount())).concat(uuid).concat(CommonConstants.PATH_SUFFIX);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
                fullPath, new ByteArrayInputStream(datas));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, SSEAlgorithm.AES256.toString());
        putObjectRequest.setMetadata(metadata);
        ossClient.putObject(putObjectRequest);
        return String.format("https://%s.%s/%s", bucket, endpoint, fullPath);
    }

    /**
     * 文件加密上传
     *
     * @param accountId
     * @param datas
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/8/16 16:30
     */
    public String uploadFileOnEncrypt(Long accountId, byte[] datas) {
        logger.info("------- 数据上传开始");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fullPath = filePathPrefix.concat("userPhoto").concat("/")
                .concat(DateUtils.getDate2String(new Date()))
                .concat("/").concat(String.valueOf(accountId))
                .concat(String.valueOf(System.currentTimeMillis()))
                .concat(String.valueOf(getCount())).concat(uuid).concat(CommonConstants.PATH_SUFFIX);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,
                fullPath, new ByteArrayInputStream(datas));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, SSEAlgorithm.AES256.toString());
        putObjectRequest.setMetadata(metadata);
        ossClient.putObject(putObjectRequest);
        return String.format("https://%s.%s/%s", bucket, endpoint, fullPath);
    }

    public void delete(String fullPath) {
        // 根据BucketName,objectName删除文件
        ossClient.deleteObject(bucketName, fullPath.replace("https://raas-image-tes.oss-cn-beijing.aliyuncs.com/", ""));
        ossClient.shutdown();
    }

    // 数据下载
    public String getFileContentFromOSS(String fullPath) throws IOException {
        logger.info("------- 数据下载开始");
        fullPath = getFullPathFromDB(fullPath);
        OSSObject object = ossClient.getObject(bucket, fullPath);
        InputStream contentInput = null;
        ByteArrayOutputStream os = null;
        try {
            contentInput = object.getObjectContent();
            os = new ByteArrayOutputStream();
            byte[] dataArr = new byte[1024 * 8];
            int length = 0;
            while ((length = contentInput.read(dataArr)) != -1) {
                os.write(dataArr, 0, length);
            }
            os.flush();
            logger.info("------- 数据下载完成");
            return new String(os.toByteArray(), "utf-8");
        } finally {
            if (contentInput != null) {
                contentInput.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    private int getCount() {
        if (count.addAndGet(1) <= COUNT_LIMIT) {
            return count.get();
        }
        lock.lock();
        try {
            if (count.addAndGet(1) <= COUNT_LIMIT) {
                return count.get();
            } else {
                count = new AtomicInteger(0);
                return count.get();
            }
        } finally {
            lock.unlock();
        }
    }
}
