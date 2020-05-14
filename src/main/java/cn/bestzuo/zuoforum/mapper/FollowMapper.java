package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.Follow;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 关注信息Mapper
 */
@Component
public interface FollowMapper {

    /**
     * 新增一条关注信息
     * @param follow
     * @return
     */
    int insertFollow(Follow follow);

    /**
     * 根据关注者的用户名和被关注者的用户名查询关注信息
     * @param userName
     * @param followName
     * @return
     */
    Follow selectFollowByUserNameAndFollowName(String userName, String followName);

    /**
     * 根据主键改变关注状态
     * @param id
     * @return
     */
    int updateFollowStatusById(int status, Integer id);

    /**
     * 根据用户名查询关注的人
     * @param username
     * @return
     */
    List<Follow> selectFollowByUsername(String username);

    /**
     * 根据用户名查询关注我的人
     * @param username
     * @return
     */
    List<Follow> selectFansByUsername(String username);
}
