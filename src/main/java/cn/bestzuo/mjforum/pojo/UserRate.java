package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 用户积分实体类
 *
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/6 14:14
 */
@Data
public class UserRate {

    //自增主键
    private int id;

    //用户Id
    private int userId;

    //用户积分
    private int rate;
}
