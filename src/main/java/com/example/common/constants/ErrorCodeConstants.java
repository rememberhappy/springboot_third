package com.example.common.constants;

/**
 * AI 阿里云 错误码及对应的错误信息常量
 */
public class ErrorCodeConstants {

    public static class ErrorCode {
        /**
         * access_token获取失败
         */
        public static final int AI_O1 = 1001;
        /**
         * verify_token获取失败
         */
        public static final int AI_O2 = 1002;
        /**
         * 获取认证人脸 -- 获取失败
         */
        public static final int AI_O3 = 1003;
        /**
         * 获取认证结果 -- 获取失败
         */
        public static final int AI_O4 = 1004;
        /**
         * 获取账户数据 -- 获取失败
         */
        public static final int AI_O5 = 1005;
        /**
         * 人脸对比 -- 获取失败
         */
        public static final int AI_O6 = 1006;
        /**
         * 保存认证结果 -- 获取失败
         */
        public static final int AI_O7 = 1007;
        /**
         * 保存人脸失败 -- 获取失败
         */
        public static final int AI_O8 = 1008;
        /**
         * 保存人脸失败 -- 获取失败
         */
        public static final int AI_O9 = 1009;
        /**
         * 未认证
         */
        public static final int AI_10 = 1010;
        /**
         * 认证未通过
         */
        public static final int AI_11 = 1011;
        /**
         * 重复认证
         */
        public static final int AI_12 = 1012;
        /**
         * 身份证识别异常
         */
        public static final int AI_13 = 1013;
        /**
         * 身份证识别异常
         */
        public static final int AI_14 = 1014;
        /**
         * 活体检测异常
         */
        public static final int AI_15 = 1015;
        /**
         * 身份信息公安验证失败
         */
        public static final int AI_16 = 1016;
        /**
         * 系统异常
         */
        public static final int AI_50000 = 50000;
    }

    public static class ErrorMessage {
        public static final String AI_O1 = "access_token异常";
        public static final String AI_O2 = "verify_token异常";
        public static final String AI_O3 = "获取认证人脸异常";
        public static final String AI_O4 = "获取认证结果异常";
        public static final String AI_O5 = "获取账户数据异常";
        public static final String AI_O6 = "人脸对比失败";
        public static final String AI_O7 = "保存认证结果失败";
        public static final String AI_O8 = "保存人脸失败";
        public static final String AI_O9 = "身份信息与已录入信息不匹配";
        public static final String AI_10 = "未认证";
        public static final String AI_11 = "认证未通过";
        public static final String AI_12 = "该账户已认证";
        public static final String AI_13 = "身份识别异常";
        public static final String AI_14 = "身份信息有误";
        public static final String AI_16 = "身份信息公安验证失败";
        public static final String AI_50000 = "系统异常";
    }
}
