package cn.bestzuo.mjforum.pojo.vo;

import lombok.Data;

@Data
public class CollectionNoticeVO {

    //自增ID
    private int id;

    //收藏的问题标题
    private String title;

    //问题ID
    private Integer questionId;

    //收藏者
    private String username;

    //用户ID
    private Integer uId;

    //收藏者头像
    private String avatar;

    //收藏时间
    private String time;
}
