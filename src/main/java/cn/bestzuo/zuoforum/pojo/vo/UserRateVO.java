package cn.bestzuo.zuoforum.pojo.vo;

import lombok.Data;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/6 14:54
 */
@Data
public class UserRateVO {

    //用户ID
    private int uid;

    //用户名
    private String username;

    //用户头像地址
    private String avatar;

    //积分
    private int rate;
}
