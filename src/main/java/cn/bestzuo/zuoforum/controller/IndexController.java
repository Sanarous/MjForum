package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.Tags;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.UserRate;
import cn.bestzuo.zuoforum.pojo.vo.UserIndexQuestionVO;
import cn.bestzuo.zuoforum.pojo.vo.UserInfoVO;
import cn.bestzuo.zuoforum.pojo.vo.UserRateVO;
import cn.bestzuo.zuoforum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页跳转控制器
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

    @Autowired
    private UserRateService userRateService;

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
     * @return
     */
    @GetMapping("/java")
    public String getIndex() {
        return "java";
    }

    /**
     * 跳转到项目简介页面
     * @return
     */
    @GetMapping("/project")
    public String getProjectIndex() {
        return "project";
    }

    /**
     * 跳转到标签搜索页面
     * @param tagname
     * @param model
     * @return
     */
    @GetMapping("/tag")
    public String getTagInfo(String tagname, Model model){
        model.addAttribute("tagName",tagname);
        return "post/tag";
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
     * 获取积分排行前几位的用户信息
     * @return 通用返回结果
     */
    @RequestMapping("/getTopRateUserInfo")
    @ResponseBody
    public ForumResult getTopRateUserInfo(){
        List<UserRate> userRates = userRateService.selectTopRateUserInfo();
        List<UserRateVO> res = new ArrayList<>();
        for(UserRate rate : userRates){
            UserInfo userInfo = userInfoService.selectUserInfoByUid(rate.getUserId());
            UserRateVO vo = new UserRateVO();
            vo.setUid(userInfo.getUId());
            vo.setUsername(userInfo.getUsername());
            vo.setRate(rate.getRate());

            //用户头像地址
            vo.setAvatar(userInfo.getAvatar());
            res.add(vo);
        }
        return new ForumResult(200,"查询成功",res);
    }

    /**
     * 将UserInfo转换成VO对象
     *
     * @param userInfo  后端用户信息
     * @return  前端展示的用户信息
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
     * @return  前端展示的问题信息
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
                if(tagInfo.getIsOriginTag() == 1){
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
