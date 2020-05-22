package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.Follow;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 关注信息Mapper
 */
@Component
public interface FollowMapper {

    /**
     * 新增一条关注信息
     *
     * @param follow 关注信息
     * @return 更新行数
     */
    int insertFollow(Follow follow);

    /**
     * 根据关注者的用户名和被关注者的用户名查询关注信息
     *
     * @param userId   被关注着ID
     * @param followId 点击关注着ID
     * @return 关注信息
     */
    Follow selectFollowByUserIdAndFollowId(Integer userId, Integer followId);

    /**
     * 根据主键改变关注状态
     *
     * @param id 主键
     * @return 更新行数
     */
    int updateFollowStatusById(int status, Integer id);

    /**
     * 根据用户名查询关注的人
     *
     * @param followId 点击关注者ID
     * @return 关注信息
     */
    List<Follow> selectFollowByFollowId(Integer followId);

    /**
     * 根据用户名查询关注我的人
     *
     * @param userId 被关注者ID
     * @return 粉丝信息
     */
    List<Follow> selectFansByUid(Integer userId);
}
