package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Follow;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.FollowService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 关注Controller
 */
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 查询关注状态
     *
     * @param userName    被关注者用户名
     * @param followName   关注者用户名
     * @return
     */
    @RequestMapping("/getFollowStatus")
    @ResponseBody
    public ForumResult getFollowStatus(@RequestParam("userName") String userName,
                                       @RequestParam("followName") String followName) {
        //后端数据校验
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(followName)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        UserInfo userInfo = userInfoService.getUserInfoByName(userName);
        UserInfo userInfo1 = userInfoService.getUserInfoByName(followName);
        if (userInfo == null || userInfo1 == null) {
            return new ForumResult(400, "用户名不存在", null);
        }

        //查询状态
        Follow follow = followService.selectFollowByUserNameAndFollowName(userName, followName);
        if (follow == null) {
            return new ForumResult(200, "查询成功", 0);
        } else {
            return new ForumResult(200, "查询成功", follow.getStatus());
        }
    }

    /**
     * 关注
     *
     * @param userName   被关注者
     * @param followName 关注者
     * @return
     */
    @RequestMapping("/follow")
    @ResponseBody
    public ForumResult follow(@RequestParam("userName") String userName,
                              @RequestParam("followName") String followName) {
        //后端数据校验
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(followName)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        if (userName.trim().equals(followName)) {
            return new ForumResult(300, "您不能关注自己哦", null);
        }

        UserInfo userInfo = userInfoService.getUserInfoByName(userName);
        UserInfo userInfo1 = userInfoService.getUserInfoByName(followName);
        if (userInfo == null || userInfo1 == null) {
            return new ForumResult(400, "用户名不存在", null);
        }

        try {
            //先查对应状态
            Follow followInfo = followService.selectFollowByUserNameAndFollowName(userName, followName);
            if (followInfo == null) {
                //第一次插入关注信息
                Follow follow = new Follow();
                follow.setUserId(userInfo.getUId());
                follow.setUserName(userName);
                follow.setFollowId(userInfo1.getUId());
                follow.setFollowName(followName);
                follow.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                follow.setStatus(1); // 1-表示已关注

                followService.insertFollow(follow);

                return new ForumResult(200, "关注成功", 1);
            } else {
                //更新关注信息
                if (followInfo.getStatus() == 0) {
                    //修改为关注状态
                    followService.updateFollowStatusById(1, followInfo.getId());
                    return new ForumResult(200, "关注成功", 1);
                } else {
                    followService.updateFollowStatusById(0, followInfo.getId());
                    return new ForumResult(200, "取消关注成功", 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "关注失败", null);
        }
    }

    /**
     * 用户展示页的关注按钮，逻辑稍微不同于普通关注按钮
     *
     * @param userName   被关注者用户名
     * @param followName   关注者用户名
     * @return
     */
    @RequestMapping("/myFollow")
    @ResponseBody
    public ForumResult myFollow(@RequestParam("userName") String userName,
                                @RequestParam("followName") String followName) {
        //后端数据校验
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(followName)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        if (userName.trim().equals(followName)) {
            return new ForumResult(300, "您不能关注自己哦", null);
        }

        UserInfo userInfo = userInfoService.getUserInfoByName(userName);
        UserInfo userInfo1 = userInfoService.getUserInfoByName(followName);
        if (userInfo == null || userInfo1 == null) {
            return new ForumResult(400, "用户名不存在", null);
        }

        try {
            Follow followInfo = followService.selectFollowByUserNameAndFollowName(userName, followName);
            if (followInfo == null) {
                //第一次插入关注信息
                Follow follow = new Follow();
                follow.setUserId(userInfo.getUId());
                follow.setUserName(userName);
                follow.setFollowId(userInfo1.getUId());
                follow.setFollowName(followName);
                follow.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                follow.setStatus(1); // 1-表示已关注

                followService.insertFollow(follow);

                return new ForumResult(200, "关注成功", 1);
            } else {
                if (followInfo.getStatus() == 1) {
                    return new ForumResult(300, "您已经关注Ta了哦~", null);
                } else {
                    //修改为关注状态
                    followService.updateFollowStatusById(1, followInfo.getId());
                    return new ForumResult(200, "关注成功", 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "", null);
        }
    }
}
