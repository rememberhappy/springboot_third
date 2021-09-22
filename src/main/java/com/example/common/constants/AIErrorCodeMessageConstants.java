package com.example.common.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云 AI 错误信息和错误编码常量信息
 */
public class AIErrorCodeMessageConstants {

    private static final Map<Integer, String> CACHE_MAP = new HashMap<>();
    private static final Map<String, String> SDK_CACHE_MAP = new HashMap<>();

    static {
        SDK_CACHE_MAP.put("SDK100", "图片大小超限");
        SDK_CACHE_MAP.put("SDK101", "图片边长不符合要求");
        SDK_CACHE_MAP.put("SDK102", "读取图片文件错误");
        SDK_CACHE_MAP.put("SDK108", "连接超时或读取数据超时");
        SDK_CACHE_MAP.put("SDK109", "不支持的图片格式");
    }

    static {
        CACHE_MAP.put(222350, "公安网图片不存在或质量过低");
        CACHE_MAP.put(222351, "身份证号与姓名不匹配");
        CACHE_MAP.put(222352, "身份证名字格式错误");
        CACHE_MAP.put(222353, "身份证号码格式错误");
        CACHE_MAP.put(222354, "公安库里不存在此身份证号");
        CACHE_MAP.put(222355, "身份证号码正确，公安库里没有对应的照片");
        CACHE_MAP.put(222356, "人脸图片质量不符合要求");
        CACHE_MAP.put(222360, "份证号码或名字非法");
        CACHE_MAP.put(222361, "公安服务连接失败,请重新尝试");
        CACHE_MAP.put(216600, "输入身份证格式错误");
        CACHE_MAP.put(216601, "身份证号和名字匹配失败");
        CACHE_MAP.put(222013, "参数格式错误");
        CACHE_MAP.put(1, "服务器内部错误,请再次请求");
        CACHE_MAP.put(2, "服务暂不可用");
        CACHE_MAP.put(3, "调用的API不存在，请检查请求URL后重新尝试");
        CACHE_MAP.put(4, "集群超限额，请再次请求");
        CACHE_MAP.put(6, "无权限访问该用户数据");
        CACHE_MAP.put(13, "获取token失败");
        CACHE_MAP.put(14, "鉴权失败");
        CACHE_MAP.put(15, "应用不存在或者创建失败");
        CACHE_MAP.put(17, "每天请求量超限额");
        CACHE_MAP.put(18, "QPS超限额");
        CACHE_MAP.put(19, "请求总量超限额");
        CACHE_MAP.put(223113, "请勿遮挡面部");
        CACHE_MAP.put(223114, "人脸图片模糊");
        CACHE_MAP.put(223115, "人脸光照不好");
        CACHE_MAP.put(223116, "请勿遮挡面部");
        CACHE_MAP.put(223118, "质量控制项错误");
        CACHE_MAP.put(223119, "活体控制项错误");
        CACHE_MAP.put(223120, "此次活体检测结果为非活体");
        CACHE_MAP.put(223121, "请勿遮挡左眼");
        CACHE_MAP.put(223122, "请勿遮挡右眼");
        CACHE_MAP.put(223123, "请勿遮挡左脸颊");
        CACHE_MAP.put(223124, "请勿遮挡右脸颊");
        CACHE_MAP.put(223125, "请勿遮挡下巴");
        CACHE_MAP.put(223126, "请勿遮挡鼻子");
        CACHE_MAP.put(223127, "请勿遮挡嘴巴");
        CACHE_MAP.put(222307, "请重新上传合法的图片");
        CACHE_MAP.put(222308, "请重新上传合法的图片");
        CACHE_MAP.put(222211, "图质量不合格");
        CACHE_MAP.put(222212, "请更换素材后重新尝试");
        CACHE_MAP.put(223129, "人脸未面向正前方");
        CACHE_MAP.put(223130, "请重新上传合法的图片");
        CACHE_MAP.put(223131, "合成图检测未通过");
        CACHE_MAP.put(222201, "服务端请求失败,重新尝试");
        CACHE_MAP.put(222202, "图片中没有人脸,检查图片质量");
        CACHE_MAP.put(222203, "无法解析人脸,检查图片质量");
        CACHE_MAP.put(222204, "下载图片失败");
        CACHE_MAP.put(222205, "服务端请求失败,重新尝试");
        CACHE_MAP.put(222206, "服务端请求失败,重新尝试");
    }

    public static String getSdkMessage(String code) {
        return SDK_CACHE_MAP.getOrDefault(code, "检测异常");
    }

    public static String getCodeMessage(Integer code) {
        return CACHE_MAP.getOrDefault(code, "操作失败");
    }
}
