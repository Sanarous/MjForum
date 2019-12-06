package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Collection;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.CollectionService;
import cn.bestzuo.zuoforum.service.QuestionService;
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
 * 问题收藏Controller
 */
@Controller
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private QuestionService questionService;


    /**
     * 获取收藏状态
     *
     * @param username  用户名
     * @param questionId  问题ID
     * @return
     */
    @RequestMapping("/getCollectionStatus")
    @ResponseBody
    public ForumResult getCollectionStatus(@RequestParam("username") String username,
                                           @RequestParam("questionId") Integer questionId) {
        //后端数据校验
        if (StringUtils.isEmpty(username) || questionId == null) {
            return new ForumResult(400, "内容不合法", null);
        }

        //后端校验用户名信息
        UserInfo userInfo = userInfoService.getUserInfoByName(username);
        if (userInfo == null)
            return new ForumResult(400, "用户不存在", null);

        Integer status = collectionService.selectCollectionStatus(username, questionId);
        if (status == null || status == 0) {
            //未收藏，显示0
            return new ForumResult(200, "", 0);
        } else {
            //已收藏
            return new ForumResult(200, "", 1);
        }
    }


    /**
     * 收藏/取消收藏问题
     *
     * @param username   用户名
     * @param questionId  问题ID
     * @return
     */
    @RequestMapping("/collect")
    @ResponseBody
    public ForumResult collectQuestion(@RequestParam("username") String username,
                                       @RequestParam("questionId") Integer questionId){
        //后端数据校验
        if (StringUtils.isEmpty(username) || questionId == null) {
            return new ForumResult(400, "内容不合法", null);
        }

        //后端校验用户名信息
        UserInfo userInfo = userInfoService.getUserInfoByName(username);
        if (userInfo == null)
            return new ForumResult(400, "用户不存在", null);

        try {
            //第一次点击时，查询收藏情况
            Collection collect = collectionService.selectSpecificCollections(username, questionId);
            if (collect == null) {
                //第一次点击，直接收藏
                Collection collection = new Collection();
                collection.setUId(userInfo.getUId());
                collection.setUsername(username);
                collection.setQuestionId(questionId);

                Question question = questionService.selectByPrimaryKey(questionId);
                collection.setPublisher(question.getPublisher());

                collection.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
                collection.setStatus(1);  // 1-已收藏

                //插入数据库,插入收藏情况
                collectionService.insertCollection(collection);
            } else {
                //找到对应的问题ID
                if (collect.getQuestionId().equals(questionId)) {
                    //有这个问题的ID
                    if (collect.getStatus() == 0) {
                        //修改收藏时间
                        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                        collectionService.updateCollectionStatus(1, username, time, questionId);
                    } else {
                        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                        collectionService.updateCollectionStatus(0, username, time, questionId);
                    }
                }
            }
            //返回状态
            int res = collectionService.selectCollectionStatus(username, questionId);
            return new ForumResult(200, "", res);
        } catch (Exception e) {
            return new ForumResult(500, "查询失败", null);
        }
    }
}
