package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 用户信息实体类
 */
@Data
public class UserInfo {
    //主键自增Id
    private Integer id;

    //对应的用户ID
    private Integer uId;

    //用户名
    private String username;

    //性别 0-男  1-女  2-保密
    private String sex;

    //生日  1970-01-01
    private String birthday;

    //邮箱
    private String email;

    //所在地区，精确到市区  湖北省-武汉市
    private String area;

    //兴趣爱好
    private String hobby;

    //个人评价
    private String comment;

    //头像地址
    private String avatar;

    //个人网站
    private String site;

    //Github链接
    private String github;

    //微博链接
    private String weibo;

    //毕业学校
    private String university;

    //所学专业
    private String majority;

    //所在公司
    private String company;

    //工作职位
    private String jobTitle;

    //用户注册时间
    private String registerDate;

    //是否公开个人信息
    private Boolean isopen;
}