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
     *
     * @return 用户信息
     */
    List<UserInfo> queryAllUserInfo();

    /**
     * 通过邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    List<UserInfo> queryUserInfoByEmail(String email);

    /**
     * 有选择的插入用户信息
     *
     * @param record 用户类
     * @return 更新行数
     */
    int insertSelective(UserInfo record);


    /**
     * 根据用户昵称查询用户信息
     *
     * @param username 用户昵称
     * @return 用户信息
     */
    UserInfo selectUserInfoByName(String username);

    /**
     * 根据用户名修改用户头像
     *
     * @param avatar   头像地址
     * @param username 用户昵称
     */
    void updateUserAvatarByName(String avatar, String username);


    /**
     * 根据用户名有选择的更新用户信息
     *
     * @param record 用户信息
     */
    void updateByUsernameSelective(UserInfo record);


    /**
     * 根据用户UID有选择的更新用户信息
     *
     * @param record 用户信息
     */
    void updateByUidSelective(UserInfo record);


    /**
     * 根据主键删除用户
     *
     * @param id 主键
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增用户信息
     *
     * @param record 用户信息
     */
    int insert(UserInfo record);

    /**
     * 根据主键查询用户信息
     *
     * @param id 主键
     * @return 用户信息
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * 获取最新的注册用户信息
     *
     * @return 用户信息
     */
    List<UserInfo> getNewUserInfo();

    /**
     * 根据用户名查询发起的问题数
     *
     * @param username 用户昵称
     * @return 问题数
     */
    Integer selectPublishedQuestionByUsername(String username);

    /**
     * 根据用户ID查询用户信息
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    UserInfo selectUserInfoByUid(Integer uid);

    /**
     * 根据UID删除用户
     *
     * @param uid 用户ID
     * @return 更新行数
     */
    int deleteUserInfoByUid(Integer uid);
}