package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.CommentReply;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.CommentReplyService;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 评论楼中楼回复Controller
 */
@Controller
public class CommentReplyController {

    @Autowired
    private CommentReplyService commentReplyService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private QuestionService questionService;

    /**
     * 楼中楼回复
     *
     * @param questionId      问题ID
     * @param username        回复者的用户名
     * @param content         回复内容
     * @param parentCommentId 父评论ID
     * @param replyfor        被回复者
     * @return
     */
    @RequestMapping("/reply")
    @ResponseBody
    public ForumResult comment(@RequestParam("questionId") String questionId,
                               @RequestParam("username") String username,
                               @RequestParam("content") String content,
                               @RequestParam("parentCommentId") Integer parentCommentId,
                               @RequestParam("replyfor") String replyfor) {

        //后端校验数据
        if (questionId == null)
            return new ForumResult(400, "关系问题失败", null);
        if (username == null)
            return new ForumResult(400, "用户未登录", null);
        if (content == null || "".equals(content.trim())) {
            return new ForumResult(400, "回复内容不能为空", null);
        }
        if (replyfor == null)
            return new ForumResult(400, "回复对象不能为空", null);

        //后端查询数据校验
        UserInfo userInfo = userInfoService.getUserInfoByName(username);  //当前回复用户
        UserInfo userInfo1 = userInfoService.getUserInfoByName(replyfor);  //目标回复用户
        if (userInfo == null || userInfo1 == null)
            return new ForumResult(500, "用户信息查询失败", null);

        int qId = Integer.parseInt(questionId);
        Question question = questionService.selectByPrimaryKey(qId);
        if (question == null)
            return new ForumResult(500, "关联问题信息失败", null);

        try {
            CommentReply commentReply = commentReplyService.insertCommentReply(qId, username, content, parentCommentId, replyfor);
            return new ForumResult(200, "回复成功", commentReply);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "回复失败，请稍后再试", null);
        }
    }

    /**
     * 查询楼中楼回复
     *
     * @param parentCommentId 父评论ID
     * @return
     */
    @RequestMapping("/queryReply")
    @ResponseBody
    public ForumResult queryReplyByCommentId(@RequestParam("parentCommentId") Integer parentCommentId) {
        List<CommentReply> commentReplies = commentReplyService.queryReplyByCommentId(parentCommentId);
        if (commentReplies.size() != 0)
            return new ForumResult(200, "查询楼中楼回复成功", commentReplies);
        else
            return new ForumResult(500, "没有回复", null);
    }

    /**
     * 查询一个评论下所有的回复数
     *
     * @param parentCommentId  父评论ID
     * @return
     */
    @RequestMapping("/getReplyNum")
    @ResponseBody
    public ForumResult queryReplyNums(@RequestParam("parentCommentId") Integer parentCommentId) {
        if (parentCommentId == null)
            return new ForumResult(400, "父评论不能为空", null);

        int num = commentReplyService.queryReplyNum(parentCommentId);
        return new ForumResult(200, "查询成功", num);
    }

}
