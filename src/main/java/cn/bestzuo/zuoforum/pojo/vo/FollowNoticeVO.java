package cn.bestzuo.zuoforum.pojo.vo;

import lombok.Data;

/**
 * 关注通知VO
 */
@Data
public class FollowNoticeVO {

    //自增主键ID
    private Integer id;

    //关注者的ID
    private Integer uid;

    //关注者的用户名
    private String username;

    //关注者头像
    private String avatar;

    //关注时间
    private String time;
}
