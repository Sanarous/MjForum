package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Comment;
import cn.bestzuo.zuoforum.pojo.CommentLike;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.CommentLikeService;
import cn.bestzuo.zuoforum.service.CommentService;
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
     * 查询某一评论下的点赞数
     *
     * @param commentId  评论ID
     * @return 包装结果
     */
    @RequestMapping("/getLikeCount")
    @ResponseBody
    public ForumResult getLikeCount(@RequestParam("commentId") Integer commentId) {
        Integer count = commentLikeService.selectLikeCountByCommentId(commentId);
        return count == null ? new ForumResult(500,"",null) : new ForumResult(200, "查询成功", count);
    }

    /**
     * 查询点赞状态
     *
     * @param username  用户名
     * @param commentId   评论ID
     * @param commentUsername   评论用户名
     * @param questionId   问题ID
     * @return 包装结果
     */
    @RequestMapping("/getLikeStatus")
    @ResponseBody
    public ForumResult getLikeStatus(@RequestParam("username") String username,
                                     @RequestParam("commentId") Integer commentId,
                                     @RequestParam("commentUsername") String commentUsername,
                                     @RequestParam("questionId") Integer questionId) {
        //后端校验
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(commentUsername)) return new ForumResult(400, "输入数据不能为空", null);

        if (commentId == null || questionId == null) return new ForumResult(400, "输入数据不能为空", null);
        //点赞者
        UserInfo userInfo = userInfoService.getUserInfoByName(username);
        //被点赞者
        UserInfo userInfo1 = userInfoService.getUserInfoByName(commentUsername);
        if (userInfo == null || userInfo1 == null) return new ForumResult(400, "用户不存在", null);
        Comment comment = commentService.selectCommentByPrimaryKey(commentId);
        if (comment == null) return new ForumResult(400, "评论信息不存在", null);
        CommentLike commentLike = commentLikeService.selectCommentLike(commentId, username);
        return commentLike == null ? ForumResult.ok() : new ForumResult(200, "查询成功", commentLike.getStatus());
    }

    /**
     * 新增一条评论点赞信息
     *
     * @param username   用户名
     * @param commentId   评论ID
     * @param commentUsername  评论用户名
     * @param questionId    问题ID
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

        //点赞者
        UserInfo userInfo = userInfoService.getUserInfoByName(username);

        //被点赞者
        UserInfo userInfo1 = userInfoService.getUserInfoByName(commentUsername);

        if (userInfo == null || userInfo1 == null) {
            return new ForumResult(400, "用户不存在", null);
        }

        Comment comment = commentService.selectCommentByPrimaryKey(commentId);
        if (comment == null) {
            return new ForumResult(400, "评论信息不存在", null);
        }

        //先查询点赞信息
        CommentLike commentLike = commentLikeService.selectCommentLike(commentId, username);
        if (commentLike == null) {
            //第一次点赞
            CommentLike like = new CommentLike();
            like.setCommentId(commentId);
            like.setCommentUid(userInfo1.getUId());
            like.setCommentName(commentUsername);
            like.setLikeId(userInfo.getUId());
            like.setLikeName(username);
            like.setStatus(1);
            like.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            like.setQuestionId(questionId);

            commentLikeService.insertCommentLike(like);
            return new ForumResult(200, "点赞成功", 1);
        } else {
            //查询信息
            if (commentLike.getStatus() == 0) {
                //点赞
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                int i = commentLikeService.updateCommentLike(1, time, commentLike.getId());
                return i > 0 ? new ForumResult(200, "更新成功", 1) : new ForumResult(500, "更新失败", null);
            } else {
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                int i = commentLikeService.updateCommentLike(0, time, commentLike.getId());
                return i > 0 ? new ForumResult(200, "更新成功", 0) : new ForumResult(500, "更新失败", null);
            }
        }
    }
}
