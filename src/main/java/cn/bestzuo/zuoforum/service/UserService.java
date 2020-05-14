package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.User;

import java.util.List;

/**
 * 用户Service
 */
public interface UserService {

    /**
     * 获取所有用户信息
     * @return
     */
    List<User> getAllUsers();

    /**
     * 新增用户信息
     * @param username 用户名
     * @param password 密码
     */
    int insertUser(String username, String password);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByName(String username);

    /**
     * 修改密码
     * @param username
     * @param oldPassword
     * @param newPassword
     * @return
     */
    int updatePassword(String username,String oldPassword,String newPassword);

    /**
     * 根据用户Id删除用户信息
     * @param id
     * @return
     */
    int deleteUserById(Integer id);

    /**
     * 根据用户ID查询用户
     * @param userId
     * @return
     */
    User getUserByUserId(Integer userId);

}
