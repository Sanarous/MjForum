package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.LayuiFlowResult;
import cn.bestzuo.mjforum.pojo.*;
import cn.bestzuo.mjforum.pojo.vo.CollectionNoticeVO;
import cn.bestzuo.mjforum.pojo.vo.CommentLikeVO;
import cn.bestzuo.mjforum.pojo.vo.CommentNoticeVO;
import cn.bestzuo.mjforum.pojo.vo.FollowNoticeVO;
import cn.bestzuo.mjforum.service.*;
import cn.bestzuo.mjforum.util.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知消息跳转
 *
 * @author zuoxiang
 * @date 2019/11/24
 */
@Controller
public class NoticeController {

    private final CollectionService collectionService;

    private final CommentNoticeService commentNoticeService;

    private final UserInfoService userInfoService;

    private final QuestionService questionService;

    private final FollowService followService;

    private final CommentLikeService commentLikeService;

    private final CommentService commentService;

    @Autowired
    public NoticeController(CollectionService collectionService, CommentNoticeService commentNoticeService, UserInfoService userInfoService, QuestionService questionService, FollowService followService, CommentLikeService commentLikeService, CommentService commentService) {
        this.collectionService = collectionService;
        this.commentNoticeService = commentNoticeService;
        this.userInfoService = userInfoService;
        this.questionService = questionService;
        this.followService = followService;
        this.commentLikeService = commentLikeService;
        this.commentService = commentService;
    }

    @GetMapping("/notice")
    public String index() {
        return "user/notice";
    }

    @GetMapping("/notice/getCommentNotice")
    public String getCommentNotice(Model model) {
        model.addAttribute("destination", "comment");
        return "user/notice";
    }

    @GetMapping("/notice/getCollectionNotice")
    public String getCollectionNotice(Model model) {
        model.addAttribute("destination", "collection");
        return "user/notice";
    }

    @GetMapping("/notice/getFollowNotice")
    public String getFollowNotice(Model model) {
        model.addAttribute("destination", "follow");
        return "user/notice";
    }

    @GetMapping("/notice/getPraiseNotice")
    public String getPraiseNotice(Model model) {
        model.addAttribute("destination", "praise");
        return "user/notice";
    }

    @GetMapping("/notice/getMajiangNotice")
    public String getMajiangNotice(Model model) {
        model.addAttribute("destination", "majiang");
        return "user/notice";
    }

    @GetMapping("/notice/getReportNotice")
    public String getReportNotice(Model model) {
        model.addAttribute("destination", "report");
        return "user/notice";
    }

    @GetMapping("/notice/getOtherNotice")
    public String getOtherNotice(Model model) {
        model.addAttribute("destination", "other");
        return "user/notice";
    }


    /**
     * 获取评论通知消息
     *
     * @return 包装结果
     */
    @GetMapping("/commentNotice")
    @ResponseBody
    public LayuiFlowResult commentNotice(@RequestParam("page") Integer page,
                                         @RequestParam("username") String username) {
        PageHelper.startPage(page, 5);
        List<CommentNoticeInfo> commentNoticeInfos = commentNoticeService.selectCommentNoticeByName(username);
        PageInfo<CommentNoticeInfo> pageInfo = new PageInfo<>(commentNoticeInfos);

        if (commentNoticeInfos.size() == 0) return new LayuiFlowResult(200, "", null, 0);

        //转换成前端VO
        List<CommentNoticeVO> res = new ArrayList<>();
        for (CommentNoticeInfo c : pageInfo.getList()) {
            res.add(convertCommentNoticeToVO(c));
        }

        System.out.println(pageInfo.getPages());
        System.out.println(pageInfo.getNextPage());
        System.out.println(pageInfo.getTotal());

        return new LayuiFlowResult(200, "查询成功", res, pageInfo.getPages());
    }

    /**
     * 将评论信息转换成前端展示的VO
     *
     * @param commentNoticeInfo 评论消息通知
     * @return 前端VO
     */
    private CommentNoticeVO convertCommentNoticeToVO(CommentNoticeInfo commentNoticeInfo) {
        CommentNoticeVO commentNoticeVO = new CommentNoticeVO();
        commentNoticeVO.setId(commentNoticeInfo.getId());

        //获取回复者头像
        UserInfo info = userInfoService.selectUserInfoByUid(commentNoticeInfo.getCommentId());
        commentNoticeVO.setCommentAvatar(info.getAvatar());
        commentNoticeVO.setParentCommentId(commentNoticeInfo.getParentCommentId());
        commentNoticeVO.setUsername(info.getUsername());
        commentNoticeVO.setContent(commentNoticeInfo.getContent());
        commentNoticeVO.setStatus(commentNoticeInfo.getStatus());
        commentNoticeVO.setQuestionId(commentNoticeInfo.getQuestionId());
        commentNoticeVO.setTime(commentNoticeInfo.getTime());

        //获取文章标题
        Question question = questionService.selectByPrimaryKey(commentNoticeInfo.getQuestionId());
        if (question.getTitle().length() > 20) {
            commentNoticeVO.setTitle(question.getTitle().substring(0, 20) + "...");
        } else {
            commentNoticeVO.setTitle(question.getTitle());
        }
        return commentNoticeVO;
    }

    /**
     * 获取收藏通知消息
     *
     * @return
     */
    @GetMapping("/collectionNotice")
    @ResponseBody
    public LayuiFlowResult collectMe(@RequestParam("page") Integer page,
                                     @RequestParam("username") String username) {

        UserInfo userInfo = userInfoService.getUserInfoByName(username);
        if (userInfo == null) return new LayuiFlowResult(400, "", 0, 0);

        //数据校验
        PageHelper.startPage(page, 5);
        List<Collection> collections = collectionService.selectCollectionByPublisher(userInfo.getUId());
        PageInfo<Collection> pageInfo = new PageInfo<>(collections);


        if (collections.size() == 0) return new LayuiFlowResult(200, "查询成功", null, 0);

        //转换前端VO
        List<CollectionNoticeVO> res = new ArrayList<>();
        for (Collection c : pageInfo.getList()) {
            res.add(convertCollectionNoticeToVO(c));
        }
        return new LayuiFlowResult(200, "查询成功", res, pageInfo.getPages());
    }

    /**
     * 将收藏通知转换成前端通知的VO
     *
     * @param collection
     * @return
     */
    private CollectionNoticeVO convertCollectionNoticeToVO(Collection collection) {
        CollectionNoticeVO vo = new CollectionNoticeVO();
        vo.setId(collection.getId());
        //获取收藏者的用户信息（头像地址）
        UserInfo userInfo = userInfoService.selectUserInfoByUid(collection.getUId());
        vo.setAvatar(userInfo.getAvatar());
        vo.setUId(userInfo.getUId());

        Question question = questionService.selectByPrimaryKey(collection.getQuestionId());
        if (question.getTitle().length() > 25) {
            vo.setTitle(question.getTitle().substring(0, 25) + "...");
        } else
            vo.setTitle(question.getTitle());

        vo.setQuestionId(question.getId());
        vo.setTime(collection.getTime());
        vo.setUsername(userInfo.getUsername());

        return vo;
    }

    /**
     * 查询关注我的人
     *
     * @param username
     * @return
     */
    @GetMapping("/followNotice")
    @ResponseBody
    public LayuiFlowResult followMe(@RequestParam("page") Integer page,
                                    @RequestParam("username") String username) {
        PageHelper.startPage(page, 5);

        List<Follow> follows = followService.selectFansByUsername(username);

        PageInfo<Follow> pageInfo = new PageInfo<>(follows);

        if (follows.size() == 0) return new LayuiFlowResult(200, "查询成功", null, 0);

        List<FollowNoticeVO> res = new ArrayList<>();
        for (Follow follow : pageInfo.getList()) {
            res.add(convertFollowToVO(follow));
        }
        return new LayuiFlowResult(200, "查询成功", res, pageInfo.getPages());
    }

    /**
     * 将关注通知消息转换成前端VO
     *
     * @param follow
     * @return
     */
    private FollowNoticeVO convertFollowToVO(Follow follow) {
        FollowNoticeVO vo = new FollowNoticeVO();
        vo.setId(follow.getId());

        UserInfo info = userInfoService.selectUserInfoByUid(follow.getFollowId());
        vo.setUsername(info.getUsername());
        vo.setUid(info.getUId());
        vo.setTime(follow.getTime());
        vo.setAvatar(info.getAvatar());

        return vo;
    }

    /**
     * 赞了我的
     *
     * @param username
     * @return
     */
    @GetMapping("/praiseNotice")
    @ResponseBody
    public LayuiFlowResult praiseMe(@RequestParam("page") Integer page,
                                    @RequestParam("username") String username) {

        PageHelper.startPage(page, 5);
        List<CommentLike> likes = commentLikeService.selectCommentLikeByUsername(username);
        PageInfo<CommentLike> pageInfo = new PageInfo<>(likes);

        if (likes.size() == 0) return new LayuiFlowResult(200, "查询成功", null, 0);

        List<CommentLikeVO> res = new ArrayList<>();
        for (CommentLike commentLike : pageInfo.getList()) {
            res.add(convertCommentLikeToVO(commentLike));
        }
        return new LayuiFlowResult(200, "查询成功", res, pageInfo.getPages());
    }

    /**
     * 将点赞信息转换成前端VO
     *
     * @param commentLike
     * @return
     */
    private CommentLikeVO convertCommentLikeToVO(CommentLike commentLike) {
        CommentLikeVO vo = new CommentLikeVO();
        vo.setId(commentLike.getId());
        vo.setUid(commentLike.getLikeId());

        UserInfo info = userInfoService.selectUserInfoByUid(commentLike.getLikeId());
        vo.setUsername(info.getUsername());
        vo.setAvatar(info.getAvatar());

        vo.setQuestionId(commentLike.getQuestionId());
        Question question = questionService.selectByPrimaryKey(commentLike.getQuestionId());
        vo.setTitle(question.getTitle());

        Comment comment = commentService.selectCommentByPrimaryKey(commentLike.getCommentId());
        String text = CommonUtils.Html2Text(comment.getComment());
        if (text.length() > 20) {
            vo.setContent(text.substring(0, 20));
        } else {
            vo.setContent(text);
        }
        vo.setTime(commentLike.getTime());

        return vo;
    }
}
