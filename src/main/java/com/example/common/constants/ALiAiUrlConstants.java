package com.example.common.constants;

/**
 * 阿里AI url 地址常量
 */
public interface ALiAiUrlConstants {

    /**
     * 【鉴权认证机制接口】。地址：https://ai.baidu.com/ai-doc/REFERENCE/Ck3dwjhhu
     * 百度AIP开放平台使用OAuth2.0授权调用开放API，调用API时必须在URL中带上access_token参数，每次请求都会生成一个新的assess_token
     * {
     * 		"refresh_token": "25.b55fe1d287227ca97aab219bb249b8ab.315360000.1798284651.282335-8574074",// access_token
     * 		"expires_in": 2592000,// Access Token的有效期(秒为单位，默认有效期30天)
     * 		"scope": "public wise_adapt",
     * 		"session_key": "9mzdDZXu3dENdFZQurfg0Vz8slgSgvvOAUebNFzyzcpQ5EnbxbF+hfG9DQkpUVQdh4p6HbQcAiz5RmuBAja1JJGgIdJI",
     * 		"access_token": "24.6c5e1ff107f0e8bcef8c46d3424a0e78.2592000.1485516651.282335-8574074",
     * 		"session_secret": "dfac94a3489fe9fca7c3221cbf7525ff"
     * }
     */
    String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    /**
     * 【阿里ocr身份证识别】。地址：https://ai.baidu.com/ai-doc/OCR/rk3h7xzck
     * 支持对二代居民身份证正反面所有8个字段进行结构化识别，包括姓名、性别、民族、出生日期、住址、身份证号、签发机关、有效期限，识别准确率超过99%；
     * 同时支持身份证正面头像检测，并返回头像切片的base64编码及位置信息。
     * 同时，支持对用户上传的身份证图片进行图像风险和质量检测，可识别图片是否为复印件或临时身份证，是否被翻拍或编辑，是否存在正反颠倒、模糊、欠曝、过曝等质量问题。
     * {
     *     "log_id": 2648325511,
     *     "direction": 0,
     *     "image_status": "normal",
     *     "photo": "/9j/4AAQSkZJRgABA......",
     *     "photo_location": {
     *         "width": 1189,
     *         "top": 638,
     *         "left": 2248,
     *         "height": 1483
     *     },
     *     "words_result": {
     *         "住址": {
     *             "location": {
     *                 "left": 267,
     *                 "top": 453,
     *                 "width": 459,
     *                 "height": 99
     *             },
     *             "words": "南京市江宁区弘景大道3889号"
     *         },
     *         "公民身份号码": {
     *             "location": {
     *                 "left": 443,
     *                 "top": 681,
     *                 "width": 589,
     *                 "height": 45
     *             },
     *             "words": "330881199904173914"
     *         },
     *         "出生": {
     *             "location": {
     *                 "left": 270,
     *                 "top": 355,
     *                 "width": 357,
     *                 "height": 45
     *             },
     *             "words": "19990417"
     *         },
     *         "姓名": {
     *             "location": {
     *                 "left": 267,
     *                 "top": 176,
     *                 "width": 152,
     *                 "height": 50
     *             },
     *             "words": "伍云龙"
     *         },
     *         "性别": {
     *             "location": {
     *                 "left": 269,
     *                 "top": 262,
     *                 "width": 33,
     *                 "height": 52
     *             },
     *             "words": "男"
     *         },
     *         "民族": {
     *             "location": {
     *                 "left": 492,
     *                 "top": 279,
     *                 "width": 30,
     *                 "height": 37
     *             },
     *             "words": "汉"
     *         }
     *     },
     *     "words_result_num": 6
     * }
     * {
     *     "words_result":{
     *         "失效日期":{
     *             "location":{
     *                 "top":244,
     *                 "left":314,
     *                 "width":78,
     *                 "height":19
     *             },
     *             "words":"20220601"
     *         },
     *         "签发机关":{
     *             "location":{
     *                 "top":214,
     *                 "left":221,
     *                 "width":79,
     *                 "height":19
     *             },
     *             "words":"沈阳大东区"
     *         },
     *         "签发日期":{
     *             "location":{
     *                 "top":243,
     *                 "left":218,
     *                 "width":79,
     *                 "height":19
     *             },
     *             "words":"20120601"
     *         }
     *     },
     *     "words_result_num":3,
     *     "image_status":"normal",
     *     "log_id":1427559953094516162
     * }
     */
    String OAUTH_ID_CARD_ISCERN_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token=";
    /**
     *
     */
    String VERIFY_TOKEN_URL = "https://aip.baidubce.com/rpc/2.0/brain/solution/faceprint/verifyToken/generate?access_token=";
    /**
     * 【实名认证后查询接口，获取认证人脸接口】。地址：https://cloud.baidu.com/doc/FACE/s/Bk8k29mmq
     * 返回进行人脸实名认证过程中进行认证的最终采集的人脸信息（仅在认证成功时返回人脸信息，认证失败返回错误码）
     * {
     *     "success": true,
     *     "result": {
     *         "image":"https://brain.baidu.com/solution/faceprint/image/query?verify_token=xxxxxx"
     *     },
     *     "log_id": "1054986003"
     * }
     */
    String OAUTH_HUMAN_FACE_URL = "https://aip.baidubce.com/rpc/2.0/brain/solution/faceprint/result/simple?access_token=";
    /**
     * 【实名认证后查询接口，获取认证结果】。地址：https://cloud.baidu.com/doc/FACE/s/Bk8k29mmq
     * 认证结果信息查询，包含身份证OCR识别信息、用户二次确认的身份证信息，活体检测信息、及用户对权威数据源图片进行比对的分数信息。
     * （仅在认证成功时返回上述信息，认证失败返回错误码）根据Verify_token返回
     * {
     *   "success": true,
     *   "result": {
     *       "verify_result": {
     *           "score": 93.7835,
     *           "liveness_score": 0.9672966,
     *           "spoofing": 0.0
     *       },
     *       "idcard_ocr_result": {
     *           "birthday": "19960216",
     *           "issue_authority": "胶南市公安局",
     *           "address": "山东省***********",
     *           "gender": "女",
     *           "nation": "汉",
     *           "expire_time": "20221103",
     *           "name": "柴*",
     *           "issue_time": "20121103",
     *           "id_card_number": "370***********5826"
     *       },
     *       "idcard_images": {
     *           "front_base64": "/9j/4AAQSkZJRgAB....",
     *           "back_base64": "/9j/4AAQSkZJRgAB...."
     *    },
     *       "idcard_confirm": {
     *           "idcard_number": "370***********5826",
     *           "name": "柴*"
     *       }
     *   },
     *   "log_id": "160931948204246"
     * }
     */
    String OAUTH_RESULT_DETAIL_URL = "https://aip.baidubce.com/rpc/2.0/brain/solution/faceprint/result/detail?access_token=";
    /**
     * 人脸对比
     */
    String HUMAN_FACE_MATCH_URL = "https://aip.baidubce.com/rest/2.0/face/v3/match?access_token=";
    /**
     * 营业执照OCR
     */
    String OAUTH_BUSINESS_LICENSE_ISCERN_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_license?access_token=";
}
