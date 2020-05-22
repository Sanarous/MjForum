package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.EmailInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 邮箱Mapper
 */
@Component
public interface EmailMapper {

    /**
     * 新增Email信息
     *
     * @param emailInfo 邮箱信息
     * @return 更新行数
     */
    int insertEmailInfo(EmailInfo emailInfo);


    /**
     * 根据uid查询邮箱信息
     *
     * @param uid 用户ID
     * @return 邮箱信息
     */
    EmailInfo selectEmailInfoByUid(Integer uid);

    /**
     * 根据邮箱信息查询验证状态
     *
     * @param uid 用户ID
     * @return 状态
     */
    Integer selectEmailCheckStatusByUid(Integer uid);

    /**
     * 根据用户名更新邮箱信息
     *
     * @param uid   * @param uid 用户ID
     * @param email 邮箱
     * @return 更新行数
     */
    int updateEmailByUid(String email, Integer uid);

    /**
     * 根据邮箱信息修改邮箱验证状态
     *
     * @param email 邮箱
     * @return 更新行数
     */
    int updateEmailStatusByEmail(@RequestParam("check") Integer check, @RequestParam("email") String email);

    /**
     * 查询所有邮箱
     *
     * @return 邮箱
     */
    List<String> queryAllEmails();

    /**
     * 根据邮箱查询邮箱信息
     *
     * @param email 邮箱
     * @return 邮箱信息
     */
    EmailInfo selectEmailInfoByEmail(String email);
}
