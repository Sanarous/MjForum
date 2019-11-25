package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.FollowMapper;
import cn.bestzuo.zuoforum.pojo.Follow;
import cn.bestzuo.zuoforum.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    /**
     * 新增一条关注信息
     * @param follow
     * @return
     */
    @Override
    @Transactional
    public int insertFollow(Follow follow) {
        followMapper.insertFollow(follow);
        return 1;
    }

    /**
     * 根据关注者的用户名和被关注者的用户名查询关注信息
     * @param userName
     * @param followName
     * @return
     */
    @Override
    public Follow selectFollowByUserNameAndFollowName(String userName, String followName) {
        return followMapper.selectFollowByUserNameAndFollowName(userName, followName);
    }

    /**
     * 根据主键改变关注状态
     * @param status
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int updateFollowStatusById(int status, Integer id) {
        return followMapper.updateFollowStatusById(status, id);
    }

    /**
     * 根据用户名查询关注的人
     * @param username
     * @return
     */
    @Override
    public List<Follow> selectFollowByUsername(String username) {
        return followMapper.selectFollowByUsername(username);
    }

    /**
     * 根据用户名查询关注我的人
     * @param username
     * @return
     */
    @Override
    public List<Follow> selectFansByUsername(String username) {
        return followMapper.selectFansByUsername(username);
    }
}
