package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 邮箱信息实体类
 */
@Data
public class EmailInfo {

    private Integer eId;

    private Integer uid;

    private String username;

    private String email;

    //是否验证，0-未验证，1-已验证
    private Integer check;
}
