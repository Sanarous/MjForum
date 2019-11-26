package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.EmailInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface EmailMapper {

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
    int updateEmailStatusByEmail(@RequestParam("check") Integer check,@RequestParam("email") String email);

    /**
     * 查询所有邮箱
     * @return
     */
    List<String> queryAllEmails();

    /**
     * 根据邮箱查询邮箱信息
     * @param email
     * @return
     */
    EmailInfo selectEmailInfoByEmail(String email);
}
