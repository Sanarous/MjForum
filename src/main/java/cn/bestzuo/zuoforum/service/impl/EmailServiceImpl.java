package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.EmailMapper;
import cn.bestzuo.zuoforum.pojo.EmailInfo;
import cn.bestzuo.zuoforum.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮箱信息
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
}
