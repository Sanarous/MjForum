package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.vo.QuestionVO;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 问题跳转Controller
 */
@Controller
public class PostController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private TagService tagService;

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

        UserInfo info = userInfoMapper.selectUserInfoByName(question.getPublisher());
        if (info.getAvatar().contains("https")) {
            vo.setAvatar(info.getAvatar());
        } else {
            String path = "https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/";
            vo.setAvatar(path + info.getAvatar());
        }

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
}
