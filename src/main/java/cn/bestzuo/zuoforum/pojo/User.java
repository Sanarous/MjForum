package cn.bestzuo.zuoforum.pojo;

import lombok.Data;

/**
 * 注册用户账号密码pojo实体类
 */
@Data
public class User {

    //用户ID  自增主键
    private Integer uid;

    //用户名
    private String username;

    //MD5盐值加密后密码
    private String password;
}
