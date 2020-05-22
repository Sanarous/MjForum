package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 收藏
 */
@Data
public class Collection {

    //主键ID
    private Integer id;

    //收藏者Id
    private Integer uId;

    //收藏的问题ID
    private Integer questionId;

    //问题发布者ID
    private Integer publisherId;

    //收藏时间
    private String time;

    //收藏状态  0-未收藏 1-已收藏
    private Integer status;
}
