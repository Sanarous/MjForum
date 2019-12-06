package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.vo.UserIndexQuestionVO;
import cn.bestzuo.zuoforum.pojo.vo.UserInfoVO;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.service.QuestionTagService;
import cn.bestzuo.zuoforum.service.TagService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 跳转控制器
 */
@Controller
public class IndexController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionTagService questionTagService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index")
    public String index1() {
        return "index";
    }

    @GetMapping("/java")
    public String getIndex() {
        return "java";
    }

    @GetMapping("/project")
    public String getProjectIndex() {
        return "project";
    }

    /**
     * 查询最新的三个用户信息
     *
     * @return
     */
    @RequestMapping("/getNewUserInfo")
    @ResponseBody
    public ForumResult getNewUserInfo() {
        List<UserInfo> newUserInfo = userInfoService.getNewUserInfo();
        List<UserInfoVO> userInfoVOList = new ArrayList<>();
        for (UserInfo userInfo : newUserInfo) {
            UserInfoVO vo = convertUserInfoToVO(userInfo);
            userInfoVOList.add(vo);
        }
        return new ForumResult(200, "", userInfoVOList);
    }

    /**
     * 将UserInfo转换成VO对象
     *
     * @param userInfo
     * @return
     */
    private UserInfoVO convertUserInfoToVO(UserInfo userInfo) {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUid(userInfo.getUId());
        userInfoVO.setUsername(userInfo.getUsername());
        if (userInfo.getAvatar().contains("https")) {
            userInfoVO.setAvatar(userInfo.getAvatar());
        } else {
            userInfoVO.setAvatar("https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/" + userInfo.getAvatar());
        }
        userInfoVO.setUniversity(userInfo.getUniversity());
        userInfoVO.setCompany(userInfo.getCompany());
        userInfoVO.setJobTitle(userInfo.getJobTitle());

        //查询发起的问题数
        Integer questionNum = userInfoService.selectPublishedQuestionByUsername(userInfo.getUsername());
        userInfoVO.setQuestionNum(questionNum);

        return userInfoVO;
    }

    /**
     * 根据浏览量查询问题信息
     *
     * @return
     */
    @RequestMapping("/getRecommendQuestion")
    @ResponseBody
    public ForumResult getQuestionInfoByViewCount() {
        try {
            List<Question> questions = questionService.selectQuestionInfoByViewCount();
            List<Question> res = new ArrayList<>();
            for (Question q : questions) {
                res.add(convertQuestionToVO(q));
            }
            return new ForumResult(200, "查询成功", res);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "查询失败", null);
        }
    }

    /**
     * 将Question转换成前端VO
     *
     * @param question 问题实体类
     * @return
     */
    private UserIndexQuestionVO convertQuestionToVO(Question question) {
        UserIndexQuestionVO vo = new UserIndexQuestionVO();
        vo.setId(question.getId());
        if (question.getTitle().length() > 20) {
            vo.setTitle(question.getTitle().substring(0, 20) + "...");
        } else
            vo.setTitle(question.getTitle());
        vo.setViewCount(question.getViewCount());
        return vo;
    }

    /**
     * 查询引用最多的tag信息
     *
     * @return
     */
    @RequestMapping("/getMostReferTag")
    @ResponseBody
    public ForumResult selectMostReferTag() {
        try {
            List<Integer> list = questionTagService.selectMostReferTag();
            List<String> tagNames = new ArrayList<>();
            for (Integer i : list) {
                //TODO
                tagNames.add(tagService.selectTagNameByTagId(i));
            }

            return new ForumResult(200, "", tagNames);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "查询失败", null);
        }
    }
}
