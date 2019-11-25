package cn.bestzuo.zuoforum.common;

import lombok.Getter;

/**
 * 状态码
 */
public enum ForumResultCode {
    //global
    OK("200", "操作成功"),
    ERROR("000001", "操作异常"),
    PASSWD_EMPTY("002007", "密码不能为空"),
    PASSWORD_ERROR("002024", "您输入的密码不正确"),
    ;

    @Getter
    private String code;

    @Getter
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    ForumResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ForumResultCode getSmsCode(String code) {
        for (ForumResultCode otpCode : ForumResultCode.values()) {
            if (otpCode.getCode().equals(code))
                return otpCode;
        }
        return ForumResultCode.ERROR;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
