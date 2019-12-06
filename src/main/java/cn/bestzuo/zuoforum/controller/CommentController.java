package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Comment;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.vo.CommentVO;
import cn.bestzuo.zuoforum.service.CommentService;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论Controller
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private QuestionService questionService;

    /**
     * 新增评论
     *
     * @param username   评论用户名
     * @param comment    评论内容
     * @param questionId 问题ID
     * @return 封装
     */
    @RequestMapping("/comment")
    @ResponseBody
    public ForumResult commentOnQuestion(
            @RequestParam("username") String username,
            @RequestParam("comment") String comment,
            @RequestParam("questionId") Integer questionId) {

        //后端数据校验
        if (username == null)
            return new ForumResult(400, "用户信息不存在", null);
        else if (comment == null || "".equals(comment.trim()))
            return new ForumResult(400, "评论内容不能为空", null);
        else if (questionId == null) {
            return new ForumResult(400, "关联问题信息不存在", null);
        }

        //判断user信息
        UserInfo userInfo = userInfoService.getUserInfoByName(username);
        if (userInfo == null)
            return new ForumResult(500, "用户信息不存在", null);

        //判断questionId
        Question question = questionService.selectByPrimaryKey(questionId);
        if (question == null) {
            return new ForumResult(500, "问题信息不存在", null);
        }

        try {
            Comment com = commentService.insertCommentByQuestionId(username, comment, questionId);
            return new ForumResult(200, "评论成功", com);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "评论失败，请稍后再试", null);
        }
    }

    /**
     * 根据问题ID查询问题一级评论信息
     *
     * @param questionId  问题ID
     * @return
     */
    @RequestMapping("/getCommentsByQuestionId")
    @ResponseBody
    public ForumResult getCommentsByQuestionId(@RequestParam("questionId") Integer questionId) {
        //后端数据校验
        if (questionId == null)
            return new ForumResult(400, "关联问题失效", null);

        List<Comment> comments = commentService.queryCommentByQuestionId(questionId);
        if (comments.size() == 0) {
            return new ForumResult(500, "该问题下没有评论", 0);
        }

        List<CommentVO> res = new ArrayList<>();
        for (Comment comment : comments) {
            res.add(convertCommentToVO(comment));
        }

        return new ForumResult(200, "查询成功", res);
    }

    /**
     * 将Comment转换成前端VO
     *
     * @param comment  评论
     * @return
     */
    private CommentVO convertCommentToVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setUid(comment.getUid());
        vo.setCId(comment.getCId());
        vo.setQuestionId(comment.getQuestionId());
        vo.setUname(comment.getUname());
        vo.setTime(comment.getTime());
        vo.setComment(comment.getComment());

        UserInfo info = userInfoService.getUserInfoByName(comment.getUname());
        //获取评论者的头像信息
        if (info.getAvatar().contains("https")) {
            vo.setAvatar(info.getAvatar());
        } else {
            vo.setAvatar("https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/" + info.getAvatar());
        }

        //工作/学校信息
        if (StringUtils.isEmpty(info.getCompany()) && StringUtils.isEmpty(info.getUniversity())) {
            vo.setInfo("&nbsp;");
        }

        if (!StringUtils.isEmpty(info.getCompany()) && !StringUtils.isEmpty(info.getUniversity())) {
            if (!StringUtils.isEmpty(info.getJobTitle())) {
                vo.setInfo(info.getCompany() + "&nbsp;·&nbsp;" + info.getJobTitle());
            } else {
                vo.setInfo(info.getCompany());
            }
        }

        if (StringUtils.isEmpty(info.getCompany())) {
            vo.setInfo(info.getUniversity());
        } else {
            if (!StringUtils.isEmpty(info.getJobTitle())) {
                vo.setInfo(info.getCompany() + "&nbsp;·&nbsp;" + info.getJobTitle());
            } else {
                vo.setInfo(info.getCompany());
            }
        }

        return vo;
    }
}
