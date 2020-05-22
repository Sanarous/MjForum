package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 楼中楼回复
 */
@Data
public class CommentReply {

    //自增主键
    private int rId;

    //回复内容
    private String rContent;

    //回复时间
    private String rTime;

    //父评论ID
    private int parentCommentId;

    //目标用户ID，即回复给谁
    private int touid;

    //目标用户名
//    private String touname;

    //用户头像
//    private String rAvatar;

    //用户id
    private int rUid;

    //用户名
//    private String rName;
}
