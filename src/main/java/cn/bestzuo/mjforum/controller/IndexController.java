package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.Question;
import cn.bestzuo.mjforum.pojo.Tags;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.UserRate;
import cn.bestzuo.mjforum.pojo.vo.UserIndexQuestionVO;
import cn.bestzuo.mjforum.pojo.vo.UserInfoVO;
import cn.bestzuo.mjforum.pojo.vo.UserRateVO;
import cn.bestzuo.mjforum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页跳转控制器
 *
 * @author zuoxiang
 * @date 2019/11/15
 */
@Controller
public class IndexController {

    private final UserInfoService userInfoService;

    private final QuestionService questionService;

    private final QuestionTagService questionTagService;

    private final TagService tagService;

    private final UserRateService userRateService;

    @Autowired
    public IndexController(UserInfoService userInfoService, QuestionService questionService, QuestionTagService questionTagService, TagService tagService, UserRateService userRateService) {
        this.userInfoService = userInfoService;
        this.questionService = questionService;
        this.questionTagService = questionTagService;
        this.tagService = tagService;
        this.userRateService = userRateService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/index")
    public String index1() {
        return "index";
    }

    /**
     * 跳转到Java页面
     *
     * @return 页面
     */
    @GetMapping("/java")
    public String getIndex() {
        return "java";
    }

    /**
     * 跳转到项目简介页面
     *
     * @return 页面
     */
    @GetMapping("/project")
    public String getProjectIndex() {
        return "project";
    }

    /**
     * 跳转到标签搜索页面
     *
     * @param tagname 标签名
     * @param model   Model
     * @return 页面
     */
    @GetMapping("/tag")
    public String getTagInfo(String tagname, Model model) {
        model.addAttribute("tagName", tagname);
        return "post/tag";
    }

    /**
     * 查询最新的三个用户信息
     *
     * @return 包装结果
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
     * 获取积分排行前几位的用户信息
     *
     * @return 通用返回结果
     */
    @RequestMapping("/getTopRateUserInfo")
    @ResponseBody
    public ForumResult getTopRateUserInfo() {
        List<UserRate> userRates = userRateService.selectTopRateUserInfo();
        List<UserRateVO> res = new ArrayList<>();
        for (UserRate rate : userRates) {
            UserInfo userInfo = userInfoService.selectUserInfoByUid(rate.getUserId());
            UserRateVO vo = new UserRateVO();
            vo.setUid(userInfo.getUId());
            vo.setUsername(userInfo.getUsername());
            vo.setRate(rate.getRate());

            //用户头像地址
            vo.setAvatar(userInfo.getAvatar());
            res.add(vo);
        }
        return new ForumResult(200, "查询成功", res);
    }

    /**
     * 将UserInfo转换成VO对象
     *
     * @param userInfo 后端用户信息
     * @return 前端展示的用户信息
     */
    private UserInfoVO convertUserInfoToVO(UserInfo userInfo) {
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUid(userInfo.getUId());
        userInfoVO.setUsername(userInfo.getUsername());
        userInfoVO.setAvatar(userInfo.getAvatar());
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
     * @return 通用返回结果
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
     * @return 前端展示的问题信息
     */
    private UserIndexQuestionVO convertQuestionToVO(Question question) {
        UserIndexQuestionVO vo = new UserIndexQuestionVO();
        vo.setId(question.getId());
        vo.setTitle(question.getTitle().length() > 20 ? question.getTitle().substring(0, 20) + "..." : question.getTitle());
        vo.setViewCount(question.getViewCount());
        return vo;
    }

    /**
     * 查询引用最多的tag信息
     *
     * @return 通用返回结果
     */
    @RequestMapping("/getMostReferTag")
    @ResponseBody
    public ForumResult selectMostReferTag() {
        try {
            List<Integer> list = questionTagService.selectMostReferTag();  //查询引用最多的标签

            //根据标签ID查询标签名
            List<String> tagNames = new ArrayList<>();
            for (Integer i : list) {
                //根据标签ID查询对应的标签信息，此处只需要默认存在的标签
                Tags tagInfo = tagService.selectTagByTagId(i);
                if (tagInfo.getIsOriginTag() == 1) {
                    tagNames.add(tagInfo.getTagsName());
                }
            }

            return new ForumResult(200, "", tagNames);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "查询失败", null);
        }
    }
}
