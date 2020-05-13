package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.UserRateMapper;
import cn.bestzuo.zuoforum.pojo.UserRate;
import cn.bestzuo.zuoforum.service.UserRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户积分service
 *
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/6 14:51
 */
@Service
public class UserRateServiceImpl implements UserRateService {

    @Autowired
    private UserRateMapper userRateMapper;

    /**
     * 查询积分排名靠前的用户
     * @return
     */
    @Override
    public List<UserRate> selectTopRateUserInfo() {
        return userRateMapper.selectTopRateUser();
    }

    /**
     * 根据用户ID查询积分
     * @param userId
     * @return
     */
    @Override
    public UserRate selectRateById(Integer userId) {
        return userRateMapper.selectRateById(userId);
    }
}
