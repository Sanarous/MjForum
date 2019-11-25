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

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private EmailMapper emailMapper;

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
     * 更新用户信息
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
     * 获取最新注册的用户信息
     * @return
     */
    @Override
    public List<UserInfo> getNewUserInfo() {
        return userInfoMapper.getNewUserInfo();
    }

    /**
     * 根据用户名查询该用户发起的问题数
     * @param username
     * @return
     */
    @Override
    public Integer selectPublishedQuestionByUsername(String username) {
        return userInfoMapper.selectPublishedQuestionByUsername(username);
    }

    /**
     * 根据用户ID查询对应的用户信息
     * @param uid
     * @return
     */
    @Override
    public UserInfo selectUserInfoByUid(Integer uid) {
        return userInfoMapper.selectUserInfoByUid(uid);
    }
}
