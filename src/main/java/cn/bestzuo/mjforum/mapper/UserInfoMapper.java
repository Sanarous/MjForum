package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户信息Mapper
 */
@Component
public interface UserInfoMapper {

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
     * 根据用户UID有选择的更新用户信息
     *
     * @param record
     * @return
     */
    void updateByUidSelective(UserInfo record);


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

    /**
     * 根据UID删除用户
     * @param uid
     * @return
     */
    int deleteUserInfoByUid(Integer uid);
}