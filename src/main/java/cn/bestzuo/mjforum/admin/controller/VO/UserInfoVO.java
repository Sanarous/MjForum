package cn.bestzuo.mjforum.admin.controller.VO;

import lombok.Data;

/**
 * 后台用户信息管理VO
 */
@Data
public class UserInfoVO{

    //对应的用户ID
    private Integer id;

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

    //是否公开个人信息
    private String isopen;
}
