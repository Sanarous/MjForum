package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.EmailInfo;
import org.springframework.web.bind.annotation.RequestParam;

public interface EmailService {

    /**
     * 新增Email信息
     * @param emailInfo
     * @return
     */
    int insertEmailInfo(EmailInfo emailInfo);

    /**
     * 根据用户名查询邮箱信息
     * @param username
     * @return
     */
    EmailInfo selectEmailInfoByUsername(String username);

    /**
     * 根据uid查询邮箱信息
     * @param uid
     * @return
     */
    EmailInfo selectEmailInfoByUid(Integer uid);

    /**
     * 根据邮箱信息查询验证状态
     * @param username
     * @return
     */
    Integer selectEmailCheckStatusByUsername(String username);


    /**
     * 根据用户名更新邮箱信息
     * @param username
     * @param email
     * @return
     */
    int updateEmailByUsername(String email,String username);

    /**
     * 根据邮箱信息修改邮箱验证状态
     * @param email
     * @return
     */
    int updateEmailStatusByEmail(Integer check,String email);
}
