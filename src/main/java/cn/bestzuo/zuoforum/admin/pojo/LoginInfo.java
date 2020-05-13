package cn.bestzuo.zuoforum.admin.pojo;

import lombok.Data;

/**
 * 管理员登录信息
 */
@Data
public class LoginInfo {
    //登录IP
    private String ip;

    //登录信息
    private String info;

    //登录时间
    private String time;
}
