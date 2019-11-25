package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.User;

import java.util.List;

public interface UserMapper {

    /**
     * 获取所有的用户
     *
     * @return list
     */
    List<User> getAllUsers();

    /**
     * 新增用户
     * @param user user对象
     * @return
     */
    int insertUser(User user);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByName(String username);

    /**
     * 修改密码
     * @param username
     * @param newPassword
     * @return
     */
    int updatePassword(String username,String newPassword);
}
