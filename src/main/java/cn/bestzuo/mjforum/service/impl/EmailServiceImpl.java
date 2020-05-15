package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.mapper.EmailMapper;
import cn.bestzuo.mjforum.pojo.EmailInfo;
import cn.bestzuo.mjforum.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 邮箱信息Service
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailMapper emailMapper;

    @Override
    public int insertEmailInfo(EmailInfo emailInfo) {
        return emailMapper.insertEmailInfo(emailInfo);
    }

    @Override
    public EmailInfo selectEmailInfoByUsername(String username) {
        return emailMapper.selectEmailInfoByUsername(username);
    }

    @Override
    public EmailInfo selectEmailInfoByUid(Integer uid) {
        return emailMapper.selectEmailInfoByUid(uid);
    }

    @Override
    public Integer selectEmailCheckStatusByUsername(String username) {
        return emailMapper.selectEmailCheckStatusByUsername(username);
    }

    @Override
    @Transactional
    public int updateEmailByUsername(String email, String username) {
        return emailMapper.updateEmailByUsername(email,username);
    }

    @Override
    @Transactional
    public int updateEmailStatusByEmail(Integer check, String email) {
        return emailMapper.updateEmailStatusByEmail(check,email);
    }

    /**
     * 查询所有验证的邮箱
     * @return
     */
    @Override
    public List<String> queryAllEmails() {
        return emailMapper.queryAllEmails();
    }

    /**
     * 根据邮箱查询邮箱信息
     * @param email
     * @return
     */
    @Override
    public EmailInfo selectEmailInfoByEmail(String email) {
        return emailMapper.selectEmailInfoByEmail(email);
    }
}
