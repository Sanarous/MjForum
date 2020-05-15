package cn.bestzuo.mjforum.pojo.vo;

import lombok.Data;

/**
 * 所有页面需要使用的用户基本信息
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/8 14:28
 */
@Data
public class UserVO {

    //用户ID，代表用户的唯一标识
    private int uid;

    //用户名
    private String username;

    //用户性别
    private String sex;

    //用户的头像地址连接
    private String avatar;
}
