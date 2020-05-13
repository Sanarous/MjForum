package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.EmailMapper;
import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * 用户信息管理Service
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private EmailMapper emailMapper;

    /**
     * 查询所有用户信息
     * @return 用户信息集合
     */
    @Override
    public List<UserInfo> queryAllUserInfo() {
        return userInfoMapper.queryAllUserInfo();
    }

    /**
     * 根据邮箱查询用户信息
     * @param email
     * @return
     */
    @Override
    public List<UserInfo> queryUserInfoByEmail(String email) {
        return userInfoMapper.queryUserInfoByEmail(email);
    }

    /**
     * 根据用户名获取用户信息
     * @param username  用户名
     * @return  用户信息
     */
    @Override
    public UserInfo getUserInfoByName(String username) {
        return userInfoMapper.selectUserInfoByName(username);
    }

    /**
     * 修改用户头像
     */
    @Override
    @Transactional
    public void updateUserAvatar(String avatar,String username) {
        userInfoMapper.updateUserAvatarByName(avatar, username);
    }

    /**
     * 根据用户名更新用户信息
     * @param userInfo 用户信息
     */
    @Override
    @Transactional
    public void updateUserInfo(UserInfo userInfo) {
        if(!StringUtils.isEmpty(userInfo.getEmail())){
            //邮箱表中的邮箱信息也需要修改
            emailMapper.updateEmailByUsername(userInfo.getEmail(),userInfo.getUsername());
        }
        userInfoMapper.updateByUsernameSelective(userInfo);
    }

    /**
     * 根据用户ID修改用户信息
     * @param userInfo 用户信息
     */
    @Override
    @Transactional
    public void updateUserInfoByUid(UserInfo userInfo) {
        //可以不修改邮箱信息
        userInfoMapper.updateByUidSelective(userInfo);
    }

    /**
     * 获取最新注册的用户信息
     * @return 用户信息
     */
    @Override
    public List<UserInfo> getNewUserInfo() {
        return userInfoMapper.getNewUserInfo();
    }

    /**
     * 根据用户名查询该用户发起的问题数
     * @param username 用户名
     * @return 问题总数
     */
    @Override
    public Integer selectPublishedQuestionByUsername(String username) {
        return userInfoMapper.selectPublishedQuestionByUsername(username);
    }

    /**
     * 根据用户ID查询对应的用户信息
     * @param uid 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfo selectUserInfoByUid(Integer uid) {
        return userInfoMapper.selectUserInfoByUid(uid);
    }

    /**
     * 根据用户ID删除对应用户信息
     * @param uid  用户ID
     * @return 数据库中变动的行数  如果 > 0 说明删除成功
     */
    @Override
    public int deleteUserInfoByUid(Integer uid) {
        return userInfoMapper.deleteUserInfoByUid(uid);
    }
}
