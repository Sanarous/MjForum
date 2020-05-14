package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户Mapper
 */
@Component
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
    int updatePassword(String username, String newPassword);

    /**
     * 根据用户Id删除用户信息
     * @param id
     * @return
     */
    int deleteUserById(Integer id);

    /**
     * 根据用户uid查询用户信息
     * @param userId
     * @return
     */
    User getUserByUserId(Integer userId);
}
