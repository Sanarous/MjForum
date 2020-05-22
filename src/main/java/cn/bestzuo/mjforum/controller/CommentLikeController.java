package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.Comment;
import cn.bestzuo.mjforum.pojo.CommentLike;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.service.CommentLikeService;
import cn.bestzuo.mjforum.service.CommentService;
import cn.bestzuo.mjforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 评论点赞Controller
 *
 * @author zuoxiang
 * @date 2019/11/21
 */
@Controller
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    private final UserInfoService userInfoService;

    private final CommentService commentService;

    @Autowired
    public CommentLikeController(CommentLikeService commentLikeService, UserInfoService userInfoService, CommentService commentService) {
        this.commentLikeService = commentLikeService;
        this.userInfoService = userInfoService;
        this.commentService = commentService;
    }

    /**
     * 查询某一评论的点赞数
     *
     * @param commentId 评论ID
     * @return 包装结果
     */
    @GetMapping("/getLikeCount")
    @ResponseBody
    public ForumResult getLikeCount(@RequestParam("commentId") Integer commentId) {
        Integer count = commentLikeService.selectLikeCountByCommentId(commentId);
        return count == null ? new ForumResult(500, "", null) : new ForumResult(200, "查询成功", count);
    }

    /**
     * 查询点赞状态
     *
     * @param username        用户名
     * @param commentId       评论ID
     * @param commentUsername 评论用户名
     * @param questionId      问题ID
     * @return 包装结果
     */
    @GetMapping("/getLikeStatus")
    @ResponseBody
    public ForumResult getLikeStatus(@RequestParam("username") String username,
                                     @RequestParam("commentId") Integer commentId,
                                     @RequestParam("commentUsername") String commentUsername,
                                     @RequestParam("questionId") Integer questionId) {
        //后端校验
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(commentUsername))
            return new ForumResult(400, "输入数据不能为空", null);

        if (commentId == null || questionId == null) return new ForumResult(400, "输入数据不能为空", null);

        //点赞者
        UserInfo userInfo = userInfoService.getUserInfoByName(username);

        //被点赞者
        UserInfo userInfo1 = userInfoService.getUserInfoByName(commentUsername);

        //前端非法操作
        if (userInfo == null || userInfo1 == null) return new ForumResult(400, "用户不存在", null);

        //查询评论信息
        Comment comment = commentService.selectCommentByPrimaryKey(commentId);
        if (comment == null) return new ForumResult(400, "评论信息不存在", null);

        CommentLike commentLike = commentLikeService.selectCommentLike(commentId, userInfo.getUId());
        return commentLike == null ? ForumResult.ok() : new ForumResult(200, "查询成功", commentLike.getStatus());
    }

    /**
     * 新增一条评论点赞信息
     *
     * @param username        用户名
     * @param commentId       评论ID
     * @param commentUsername 评论用户名
     * @param questionId      问题ID
     * @return 包装结果
     */
    @RequestMapping("/like")
    @ResponseBody
    public ForumResult like(@RequestParam("username") String username,
                            @RequestParam("commentId") Integer commentId,
                            @RequestParam("commentUsername") String commentUsername,
                            @RequestParam("questionId") Integer questionId) {

        //后端校验
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(commentUsername)) {
            return new ForumResult(400, "输入数据不能为空", null);
        }
        if (commentId == null || questionId == null) {
            return new ForumResult(400, "输入数据不能为空", null);
        }

        //处理点赞信息
        return commentLikeService.processCommentLike(username, commentId, commentUsername, questionId);
    }
}
