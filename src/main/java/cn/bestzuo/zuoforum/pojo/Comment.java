package cn.bestzuo.zuoforum.pojo;

import lombok.Data;

/**
 * 评论实体类
 */
@Data
public class Comment {
    //主键ID
    private Integer cId;

    //评论内容
    private String comment;

    //回复时间
    private String time;

    //回复者头像地址
    private String avatar;

    //评论者ID
    private int uid;

    //评论者用户名
    private String uname;

    //关联的问题ID
    private int questionId;
}
