package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.service.CollectionService;
import cn.bestzuo.mjforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;


/**
 * 问题收藏Controller
 *
 * @author zuoxiang
 * @date 2019/11/23
 */
@Controller
public class CollectionController {

    private final CollectionService collectionService;

    private final UserInfoService userInfoService;


    @Autowired
    public CollectionController(CollectionService collectionService, UserInfoService userInfoService) {
        this.collectionService = collectionService;
        this.userInfoService = userInfoService;
    }


    /**
     * 获取收藏状态 已收藏/未收藏
     *
     * @param username   用户名
     * @param questionId 问题ID
     * @return 包装结果
     */
    @GetMapping("/getCollectionStatus")
    @ResponseBody
    public ForumResult getCollectionStatus(@RequestParam("username") String username,
                                           @RequestParam("questionId") Integer questionId) {
        //后端数据校验
        if (StringUtils.isEmpty(username) || questionId == null) return new ForumResult(400, "内容不合法", null);

        //后端校验用户名信息
        UserInfo userInfo = userInfoService.getUserInfoByName(username);
        if (userInfo == null) return new ForumResult(400, "用户不存在", null);

        Integer status = collectionService.selectCollectionStatus(userInfo.getUId(), questionId);
        return (status == null || status == 0) ? new ForumResult(200, "查询成功", 0) : new ForumResult(200, "查询成功", 1);
    }


    /**
     * 收藏/取消收藏问题
     *
     * @param username   用户名
     * @param questionId 问题ID
     * @return 包装结果
     */
    @RequestMapping("/collect")
    @ResponseBody
    public ForumResult collectQuestion(@RequestParam("username") String username,
                                       @RequestParam("questionId") Integer questionId) {
        //后端数据校验
        if (StringUtils.isEmpty(username) || questionId == null) {
            return new ForumResult(400, "内容不合法", null);
        }

        //处理点击收藏按钮的业务逻辑，返回的是收藏状态  -1 — 存在问题  0 - 未收藏  1 - 已收藏
        int res = collectionService.processCollect(username, questionId);

        //返回状态
        return res != -1 ? new ForumResult(200, "", res) : new ForumResult(500, "收藏失败", null);
    }
}
