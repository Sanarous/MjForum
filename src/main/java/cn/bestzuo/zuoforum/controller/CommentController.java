package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Comment;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.CommentService;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

        try{
            Comment com = commentService.insertCommentByQuestionId(username,comment,questionId);
            return new ForumResult(200, "评论成功", com);
        }catch (Exception e){
            e.printStackTrace();
            return new ForumResult(500, "评论失败，请稍后再试", null);
        }
    }

    /**
     * 根据问题ID查询问题一级评论信息
     * @param questionId
     * @return
     */
    @RequestMapping("/getCommentsByQuestionId")
    @ResponseBody
    public ForumResult getCommentsByQuestionId(@RequestParam("questionId") Integer questionId){
        //后端数据校验
        if(questionId == null)
            return new ForumResult(400,"关联问题失效",null);

        List<Comment> comments = commentService.queryCommentByQuestionId(questionId);
        if(comments.size() == 0){
            return new ForumResult(500,"该问题下没有评论",0);
        }

        return new ForumResult(200,"查询成功",comments);
    }
}
