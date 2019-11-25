package cn.bestzuo.zuoforum.pojo.vo;

import lombok.Data;

/**
 * 用户首页信息展示
 */
@Data
public class UserIndexInfoVO {

    //用户ID
    private Integer id;

    //用户名
    private String username;

    //头像
    private String avatar;

    //关注
    private Integer follow;

    //粉丝
    private Integer fans;

    //问题数
    private Integer questionNum;

    //收获点赞数
    private Integer likeCount;
}
