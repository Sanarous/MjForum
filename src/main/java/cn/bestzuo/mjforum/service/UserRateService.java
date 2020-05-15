package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.UserRate;

import java.util.List;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/6 14:50
 */
public interface UserRateService {

    /**
     * 查询积分排行靠前的用户信息
     * @return
     */
    List<UserRate> selectTopRateUserInfo();

    /**
     * 根据用户ID查询积分
     * @param userId
     * @return
     */
    UserRate selectRateById(Integer userId);
}
