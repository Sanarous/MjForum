package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.EmailMapper;
import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.mapper.UserMapper;
import cn.bestzuo.zuoforum.pojo.EmailInfo;
import cn.bestzuo.zuoforum.pojo.User;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.UserService;
import cn.bestzuo.zuoforum.util.MD5Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

import static cn.bestzuo.zuoforum.util.MD5Password.md5Password;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private EmailMapper emailMapper;

    /**
     * 获取所有用户信息
     *
     * @return
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = userMapper.getAllUsers();
        return users;
    }

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 密码
     */
    @Override
    @Transactional
    public int insertUser(String username, String password) {
        //后端校验
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
            return -1;

        if (username.length() < 2 || username.length() > 12)
            return -1;

        //用户名重复
        User userByName = userMapper.getUserByName(username);
        if (userByName != null)
            return -2;  //代表用户名重复

        //插入 用户名-密码 数据库中 密码加密
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password(password));
        userMapper.insertUser(user);

        //插入用户信息表中
        UserInfo userInfo = new UserInfo();
        userInfo.setUId(user.getUid());
        userInfo.setUsername(username);
        userInfo.setAvatar("https://bestzuo.cn/images/forum/anonymous.jpg");

        userInfoMapper.insertSelective(userInfo);

        //插入邮箱表
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setUid(user.getUid());
        emailInfo.setUsername(username);
        emailInfo.setCheck(0);

        emailMapper.insertEmailInfo(emailInfo);

        //注册成功
        return 1;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    /**
     * 修改密码
     * @param username  用户名
     * @param newPassword  新密码
     * @return
     */
    @Override
    @Transactional
    public int updatePassword(String username,String oldPassword,String newPassword) {
        //数据校验
        User user = userMapper.getUserByName(username);
        if(!MD5Password.verify(oldPassword,user.getPassword())){
            return -1;
        }

        //执行更新
        userMapper.updatePassword(username,MD5Password.md5Password(newPassword));
        return 1;
    }
}
