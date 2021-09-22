package com.example.common.constants;

/**
 * redis key常量定义
 */
public interface RedisKeyConstants {

    // baidu
    String USER_OAUTH_SUCC_UPDATE_TABLA_FAILTURE_TOPIC = "M_USER_OAUTH_SUCC_UPDATE_TABLA_FAILTURE_TOPIC";
    String AI_VERIFY_TOKEN_UPDATE_FAILTURE_SET = "M_VERIFY_TOKEN_UPDATE_FAILTURE_LIST";
    String AI_ACCESS_TOKEN_KEY = "M_ACCESS_TOKEN_KEY";

    // 做锁
    String AI_ACCESS_TOKEN_LABEL = "M_ACCESS_TOKEN_LABEL";
    String REDIS_TOKEN_KEY_EXIST_LOCK = "M_REDIS_TOKEN_KEY_EXIST_LOCK";

    // ali
    String ALI_HUMAN_FACE_COMPARE_KEY = "M_ALI_HUMAN_FACE_COMPARE_";
}
