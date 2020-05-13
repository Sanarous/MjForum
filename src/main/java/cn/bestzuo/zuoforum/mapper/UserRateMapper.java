package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.UserRate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户积分Mapper
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/6 14:13
 */
@Component
public interface UserRateMapper {

    /**
     * 新增用户积分信息
     * @param userRate
     * @return
     */
    int insertUserRate(UserRate userRate);

    /**
     * 根据用户ID查询用户积分
     * @param userId
     * @return
     */
    UserRate selectRateById(Integer userId);

    /**
     * 查询积分最多的前十名用户信息
     * @return
     */
    List<UserRate> selectTopRateUser();

    /**
     * 根据用户ID更新用户积分
     */
    int updateRateById(Integer userId,Integer rate);
}
