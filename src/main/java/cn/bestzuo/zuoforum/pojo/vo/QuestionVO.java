package cn.bestzuo.zuoforum.pojo.vo;

import cn.bestzuo.zuoforum.pojo.Question;
import lombok.Data;

/**
 * 问题实体的扩展
 */
@Data
public class QuestionVO extends Question {

    //问题ID
    private Integer id;

    //标题
    private String title;

    //发布时间
    private String gmtCreate;

    //修改时间
    private String gmtModified;

    //发帖人
    private String publisher;

    //评论数
    private Integer commentCount;

    //浏览数
    private Integer viewCount;

    //点赞数
    private Integer likeCount;

    //标签
    private String tag;

    //描述信息
    private String description;

    //楼主头像地址
    private String avatar;

    //发起者ID
    private Integer uid;

    //发起者职业
    private String jobTitle;

    //用户积分等级
    private String rate;

    //用户积分
    private int rateScore;

    //发起者自我介绍
    private String userInfo;

    //问题是否被加精  0-否 1-是
    private int isJing;

    //问题是否被置顶  0-否 1-是
    private int isDing;
}
