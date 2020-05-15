package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.UserRate;

import java.util.List;

public interface UserInfoService {

    /**
     * 查询所有的用户信息
     * @return
     */
    List<UserInfo> queryAllUserInfo();

    /**
     * 通过邮箱查询用户信息
     * @param email
     * @return
     */
    List<UserInfo> queryUserInfoByEmail(String email);

    /**
     * 根据用户名获取用户信息
     */
    UserInfo getUserInfoByName(String username);

    /**
     * 修改用户头像
     */
    void updateUserAvatar(String avatar,String username);

    /**
     * 更新用户信息
     * @param userInfo 用户信息
     */
    void updateUserInfo(UserInfo userInfo);


    /**
     * 根据用户ID更新用户信息
     * @param userInfo 用户信息
     */
    void updateUserInfoByUid(UserInfo userInfo);

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

    /**
     * 根据UID删除用户
     * @param uid
     * @return
     */
    int deleteUserInfoByUid(Integer uid);
}
