package cn.bestzuo.mjforum.pojo.vo;

import lombok.Data;

@Data
public class FollowVO {

    //被关注者用户ID
    private Integer id;

    //被关注者用户名
    private String username;

    //被关注者头像
    private String avatar;

    //被关注者信息
    private String info;

    //被关注者 关注的人
    private Integer follow;

    //被关注者的粉丝
    private Integer fans;

    //被关注着发起的问题
    private Integer questionNum;

    //被关注者获取的点赞总数
    private Integer likeCount;
}
