package com.example.common.constants;


import com.example.common.bo.BussinessBo;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * AI 身份证图片信息识别 错误信息敞亮
 */
public class AIIdCardDiscernErrorInfoConstants {

    private static final Map<String, String> CACHE_STRING_MAP = new HashMap<>();
    private static final Map<Integer, String> CACHE_TYPE_MAP = new HashMap<>();

    private static final String IDCARD_DISCERN_STATUS_NORMAL = "normal";
    private static final int TYPE = 1;
    private static final String ERROR = "识别失败";

    /*
     * 身份证号错误信息
     */
    static {
        // cacheStringMap.put("normal", "识别正常");
        CACHE_TYPE_MAP.put(-1, "身份证正面所有字段全为空");
        CACHE_TYPE_MAP.put(0, "身份证证号识别错误");
        CACHE_TYPE_MAP.put(1, "身份证证号和性别、出生信息一致");
        CACHE_TYPE_MAP.put(2, "身份证证号和性别、出生信息都不一致");
        CACHE_TYPE_MAP.put(3, "身份证证号和出生信息不一致");
        CACHE_TYPE_MAP.put(4, "身份证证号和性别信息不一致");
    }

    /*
     * 身份证图片错误信息
     */
    static {
        CACHE_STRING_MAP.put("normal", "识别正常");
        CACHE_STRING_MAP.put("reversed_side", "身份证正反面颠倒");
        CACHE_STRING_MAP.put("non_idcard", "上传的图片中不包含身份证");
        CACHE_STRING_MAP.put("blurred", "身份证模糊");
        CACHE_STRING_MAP.put("other_type_card", "其他类型证照");
        CACHE_STRING_MAP.put("over_exposure", "身份证关键字段反光或过曝");
        CACHE_STRING_MAP.put("over_dark", "身份证欠曝（亮度过低）");
        CACHE_STRING_MAP.put("unknown", "未知状态");
    }

    public static String getTypeMessage(int code) {
        return CACHE_TYPE_MAP.getOrDefault(code, ERROR);
    }

    public static String getCodeMessage(String code) {
        return CACHE_STRING_MAP.getOrDefault(code, ERROR);
    }

    /**
     * 检查图片状态
     *
     * @param imageStatus 调用阿里云 OCR 身份证识别返回的图片状态
     * @return com.example.common.bo.BussinessBo
     * @Author zhangdj
     * @date 2021/8/16 10:53
     */
    public static BussinessBo checkImageStatus(String imageStatus) {
        if (StringUtils.isBlank(imageStatus)) {
            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_13, ERROR);
        }
        if (!IDCARD_DISCERN_STATUS_NORMAL.equals(imageStatus)) {
            return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_13, getCodeMessage(imageStatus));
        }
        return BussinessBo.success();
    }

    /**
     * 检查身份证号
     *
     * @param type 调用阿里云 OCR 身份证识别返回的图片状态
     * @return com.example.common.bo.BussinessBo
     * @Author zhangdj
     * @date 2021/8/16 11:07
     */
    public static BussinessBo checkIdNumType(Integer type) {
        if (type == TYPE) {
            return BussinessBo.success();
        }
        return BussinessBo.error(ErrorCodeConstants.ErrorCode.AI_13, getTypeMessage(type));
    }
}
