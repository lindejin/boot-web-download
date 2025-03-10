package com.rch.download.config.response;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *       返回错误枚举类型
 * </p>
 *
 * @Author rch
 * @Date 2023-01-05
 **/
public enum ErrorCodeEnum {
    // 按照原本的 RetCode 设计的填写
    RETURN_SUCCESS(0, "响应成功"),
    UNKNOWN_ERROR(2000,"服务器开小差了....,请稍后再试！"),
    METHOD_ARGUMENT_NOT_VALID(2001,"数据验证失败异常"),
    VALIDATION_ERROR(2002,"参数验证失败"),
    HANDLE_BIND_ERROR(2007,"参数绑定失败"),
    MESSAGE_NOT_READABLE(2008,"参数解析失败"),
    //    MISSING_REQUEST_HEADER(2009,"缺少请求头参数"),
    NO_HANDLER_FOUND(2010,"Not Found"),
    METHOD_NOT_SUPPORTED(2011,"不支持当前请求方法"),
    MEDIA_TYPE_NOT_SUPPORTED(2012,"不支持当前媒体类型");

    private int code;

    private String desc;
    /** 初始化 */
    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    /** 获取错误状态码 */
    public int getCode() {
        return code;
    }
    /** 设置错误状态码 */
    public void setCode(int code) {
        this.code = code;
    }
    /** 获取错误描述 */
    public String getDesc() {
        return desc;
    }
    /** 设置错误描述 */
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
