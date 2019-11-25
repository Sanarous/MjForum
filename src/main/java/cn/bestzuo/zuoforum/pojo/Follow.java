package cn.bestzuo.zuoforum.pojo;

import lombok.Data;

/**
 * 关注表
 */
@Data
public class Follow {

    //自增ID
    private Integer id;

    //被关注者ID
    private Integer userId;

    //被关注者用户名
    private String userName;

    //点击关注着ID
    private Integer followId;

    //点击关注着用户名
    private String followName;

    //关注时间
    private String time;

    //关注状态
    private int status;
}
