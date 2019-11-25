package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.UserInfo;

import java.util.List;

public interface UserInfoMapper {

    /**
     * 有选择的插入用户信息
     *
     * @param record
     * @return
     */
    int insertSelective(UserInfo record);


    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    UserInfo selectUserInfoByName(String username);

    /**
     * 根据用户名修改用户头像
     */
    void updateUserAvatarByName(String avatar, String username);


    /**
     * 根据用户名有选择的更新用户信息
     *
     * @param record
     * @return
     */
    void updateByUsernameSelective(UserInfo record);


    /**
     * 根据主键删除用户
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增用户信息
     *
     * @param record
     * @return
     */
    int insert(UserInfo record);

    /**
     * 根据主键查询用户信息
     *
     * @param id
     * @return
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * 获取最新的注册用户信息
     * @return
     */
    List<UserInfo> getNewUserInfo();

    /**
     * 根据用户名查询发起的问题数
     * @param username
     * @return
     */
    Integer selectPublishedQuestionByUsername(String username);

    /**
     * 根据用户ID查询用户信息
     * @param uid
     * @return
     */
    UserInfo selectUserInfoByUid(Integer uid);
}