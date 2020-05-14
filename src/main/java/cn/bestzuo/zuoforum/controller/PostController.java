package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.QuestionEdit;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.UserRate;
import cn.bestzuo.zuoforum.pojo.vo.QuestionVO;
import cn.bestzuo.zuoforum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题跳转Controller
 *
 * @author zuoxiang
 * @date 2019/11/16
 */
@Controller
public class PostController {

    private final QuestionService questionService;

    private final UserInfoService userInfoService;

    private final UserRateService userRateService;

    private final TagService tagService;

    private final QuestionReportService questionReportService;

    private final QuestionEditService questionEditService;

    @Autowired
    public PostController(QuestionService questionService, UserInfoService userInfoService, UserRateService userRateService, TagService tagService, QuestionReportService questionReportService, QuestionEditService questionEditService) {
        this.questionService = questionService;
        this.userInfoService = userInfoService;
        this.userRateService = userRateService;
        this.tagService = tagService;
        this.questionReportService = questionReportService;
        this.questionEditService = questionEditService;
    }

    /**
     * 点击进入问题详情
     *
     * @param id
     * @return
     */
    @RequestMapping("/question/{id}")
    public String getPost(@PathVariable("id") Integer id, Model model) {
        Question question = questionService.selectByPrimaryKey(id);
        if (question != null) {
            questionService.updateByPrimaryKeySelective(question);
            QuestionVO res = convertToQuestionVO(question);

            model.addAttribute("question", res);
            return "post/post";
        }
        return "redirect:/";
    }

    /**
     * 将问题转换成前端VO
     *
     * @param question
     * @return
     */
    private QuestionVO convertToQuestionVO(Question question) {
        QuestionVO vo = new QuestionVO();

        vo.setId(question.getId());
        vo.setTitle(question.getTitle());
        vo.setDescription(question.getDescription());
        vo.setTag(question.getTag());
        vo.setPublisher(question.getPublisher());
        vo.setCommentCount(question.getCommentCount());
        vo.setViewCount(question.getViewCount());
        vo.setLikeCount(question.getLikeCount());
        vo.setGmtCreate(question.getGmtCreate());
        vo.setGmtModified(question.getGmtModified());

        UserInfo info = userInfoService.getUserInfoByName(question.getPublisher());
        vo.setUid(info.getUId());
        if(info.getComment() == null || info.getComment() == "" || info.getComment().length() == 0){
            vo.setUserInfo("Ta还没有自我介绍哦");
        }else{
            if(info.getComment().length() > 25){
                vo.setUserInfo(info.getComment().substring(0,25) + "...");
            }else{
                vo.setUserInfo(info.getComment());
            }
        }

        vo.setAvatar(info.getAvatar());

        if (StringUtils.isEmpty(info.getCompany()) && StringUtils.isEmpty(info.getUniversity())) {
            vo.setJobTitle("");
        }

        if (!StringUtils.isEmpty(info.getCompany()) && !StringUtils.isEmpty(info.getUniversity())) {
            if (!StringUtils.isEmpty(info.getJobTitle())) {
                vo.setJobTitle(info.getCompany() + "&nbsp;·&nbsp;" + info.getJobTitle());
            } else {
                vo.setJobTitle(info.getCompany());
            }
        }

        if (StringUtils.isEmpty(info.getCompany())) {
            vo.setJobTitle(info.getUniversity());
        } else {
            if (!StringUtils.isEmpty(info.getJobTitle())) {
                vo.setJobTitle(info.getCompany() + "&nbsp;·&nbsp;" + info.getJobTitle());
            } else {
                vo.setJobTitle(info.getCompany());
            }
        }

        //判断用户积分等级
        UserRate userRate = userRateService.selectRateById(userInfoService.getUserInfoByName(question.getPublisher()).getUId());
        if(userRate == null){
            vo.setRate("暂无");
            vo.setRateScore(0);
        }else{
            vo.setRateScore(userRate.getRate());
            if(userRate.getRate() < 5)
                vo.setRate("码奴");
            else if(userRate.getRate() >= 5 && userRate.getRate() < 15)
                vo.setRate("码徒");
            else if(userRate.getRate() >= 15 && userRate.getRate() < 50)
                vo.setRate("码农");
            else if(userRate.getRate() >= 50 && userRate.getRate() < 200)
                vo.setRate("码师");
            else if(userRate.getRate() >= 200 && userRate.getRate() < 500)
                vo.setRate("码神");
            else
                vo.setRate("码圣");
        }

        //查询该问题置顶和加精情况
        QuestionEdit questionEdit = questionEditService.selectQuestionEditById(question.getId());
        if(questionEdit == null){
            vo.setIsJing(0);
            vo.setIsJing(0);
        }else{
            vo.setIsJing(questionEdit.getIsJing());
            vo.setIsDing(questionEdit.getIsDing());
        }

        return vo;
    }

    /**
     * 获取论坛推荐问题
     *
     * @return
     */
    @RequestMapping("/getForumRecommendQuestions")
    @ResponseBody
    public ForumResult getRecommendQuestions() {
        try {
            List<Question> questions = questionService.selectForumRecommendQuestions();
            return new ForumResult(200, "查询成功", questions);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "查询失败", null);
        }
    }

    /**
     * 获取精品问题
     * @return
     */
    @RequestMapping("/getJingQuestions")
    @ResponseBody
    public ForumResult getJingQuestions(){
        List<QuestionEdit> questionEdits = questionEditService.queryAllJingQuestions();
        List<Question> res = new ArrayList<>();
        for(QuestionEdit questionEdit : questionEdits){
            Question question = questionService.selectByPrimaryKey(questionEdit.getQuestionId());
            res.add(question);
        }
        return new ForumResult(200,"查询成功",res);
    }

    /**
     * 获取相关问题推荐
     *
     * @param questionId
     * @return
     */
    @RequestMapping("/getRelatedQuestions")
    @ResponseBody
    public ForumResult getRelatedQuestions(@RequestParam("questionId") Integer questionId) {
        if (questionId == null) {
            return new ForumResult(400, "问题ID不能为空", null);
        }

        //根据问题ID查询到对应的标签
        List<Integer> integers = tagService.selectTagIDsByQuestionId(questionId);
        if (integers.size() == 0) {
            return new ForumResult(200, "查询成功", null);
        }

        //根据标签ID查询除本问题ID外的问题ID
        List<Integer> res = new ArrayList<>();
        for (Integer i : integers) {
            List<Integer> questionIds = tagService.selectQuestionIdsByTagId(i);
            if (questionIds.size() != 0) {
                for (Integer qId : questionIds) {
                    if (!qId.equals(questionId)) {
                        //最终的问题ID加到list中
                        res.add(qId);
                    }
                }
            }
        }

        //查询问题ID，返回问题信息
        if (res.size() != 0) {
            List<Question> relatedQuestions = new ArrayList<>();
            for (Integer qId : res) {
                Question question = questionService.selectByPrimaryKey(qId);
                relatedQuestions.add(question);
            }
            return new ForumResult(200, "查询成功", relatedQuestions);
        } else {
            return new ForumResult(200, "查询成功", null);
        }
    }

    /**
     * 用户举报问题处理
     * @param username  举报用户名称
     * @param rUsername   被举报用户名称
     * @param rQuestionId  被举报问题ID
     * @param reason   举报理由
     * @return
     */
    @RequestMapping("/userReport")
    @ResponseBody
    public ForumResult userReport(@RequestParam("username") String username,
                                  @RequestParam("rUsername") String rUsername,
                                  @RequestParam("rQuestionId") Integer rQuestionId,
                                  @RequestParam("reportReason") String reason){
        if(username == null || rUsername == null || rQuestionId == null || reason == null || reason.length() == 0
                || username.length() == 0 || rUsername.length() == 0){
            return new ForumResult(400,"举报信息不能为空",null);
        }

        return questionReportService.processReport(username,rUsername,rQuestionId,reason);
    }
}
