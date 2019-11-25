package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.*;
import cn.bestzuo.zuoforum.pojo.vo.*;
import cn.bestzuo.zuoforum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 通知消息跳转
 */
@Controller
public class NoticeController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private CommentNoticeService commentNoticeService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FollowService followService;

    @Autowired
    private CommentLikeService commentLikeService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/notice")
    public String index() {
        return "user/notice";
    }

    /**
     * 获取评论通知消息
     *
     * @return
     */
    @RequestMapping("/commentNotice")
    @ResponseBody
    public ForumResult commentNotice(@RequestParam("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        List<CommentNoticeInfo> commentNoticeInfos = commentNoticeService.selectCommentNoticeByName(username);
        if (commentNoticeInfos == null || commentNoticeInfos.size() == 0) {
            return new ForumResult(200, "", null);
        }

        //转换成前端展示VO
        List<CommentNoticeVO> res = new ArrayList<>();
        for (CommentNoticeInfo noticeInfo : commentNoticeInfos) {
            CommentNoticeVO noticeVO = convertCommentNoticeToVO(noticeInfo);
            res.add(noticeVO);
        }

        return new ForumResult(200, "查询成功", res);
    }

    /**
     * 将评论信息转换成前端展示的VO
     *
     * @param commentNoticeInfo
     * @return
     */
    private CommentNoticeVO convertCommentNoticeToVO(CommentNoticeInfo commentNoticeInfo) {
        CommentNoticeVO commentNoticeVO = new CommentNoticeVO();
        commentNoticeVO.setId(commentNoticeInfo.getId());

        //获取回复者头像
        UserInfo info = userInfoService.getUserInfoByName(commentNoticeInfo.getCommentName());
        if (info.getAvatar().contains("https")) {
            commentNoticeVO.setCommentAvatar(info.getAvatar());
        } else {
            commentNoticeVO.setCommentAvatar("https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/" + info.getAvatar());
        }

        commentNoticeVO.setParentCommentId(commentNoticeInfo.getParentCommentId());
        commentNoticeVO.setUsername(commentNoticeInfo.getCommentName());
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
    @RequestMapping("/collectionNotice")
    @ResponseBody
    public ForumResult collectMe(@RequestParam("username") String username) {
        //数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        UserInfo info = userInfoService.getUserInfoByName(username);
        if (info == null) {
            return new ForumResult(400, "用户不存在", null);
        }

        //查询数据库
        List<Collection> collections = collectionService.selectCollectionByPublisher(username);
        if (collections == null || collections.size() == 0) {
            //没有被收藏
            return new ForumResult(200, "查询成功", null);
        } else {
            //转换前端VO
            List<CollectionNoticeVO> collectionNoticeVOS = new ArrayList<>();
            for (Collection c : collections) {
                collectionNoticeVOS.add(convertCollectionNoticeToVO(c));
            }
            return new ForumResult(200, "查询成功", collectionNoticeVOS);
        }
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
        UserInfo userInfo = userInfoService.getUserInfoByName(collection.getUsername());
        if (userInfo.getAvatar().contains("https")) {
            vo.setAvatar(userInfo.getAvatar());
        } else {
            vo.setAvatar("https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/" + userInfo.getAvatar());
        }
        vo.setUId(userInfo.getUId());

        Question question = questionService.selectByPrimaryKey(collection.getQuestionId());
        if (question.getTitle().length() > 25) {
            vo.setTitle(question.getTitle().substring(0, 25) + "...");
        } else
            vo.setTitle(question.getTitle());

        vo.setQuestionId(question.getId());
        vo.setTime(collection.getTime());
        vo.setUsername(collection.getUsername());

        return vo;
    }

    /**
     * 查询关注我的人
     *
     * @param username
     * @return
     */
    @RequestMapping("/followNotice")
    @ResponseBody
    public ForumResult followMe(@RequestParam("username") String username) {
        //数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        UserInfo info = userInfoService.getUserInfoByName(username);
        if (info == null) {
            return new ForumResult(400, "用户不存在", null);
        }

        List<Follow> follows = followService.selectFansByUsername(username);
        if (follows.size() == 0) {
            return new ForumResult(200, "查询成功", null);
        } else {
            List<FollowNoticeVO> res = new ArrayList<>();
            for (Follow follow : follows) {
                res.add(convertFollowToVO(follow));
            }
            return new ForumResult(200, "查询成功", res);
        }
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
        vo.setUsername(follow.getFollowName());

        UserInfo info = userInfoService.getUserInfoByName(follow.getFollowName());
        vo.setUid(info.getUId());
        vo.setTime(follow.getTime());
        if (info.getAvatar().contains("https")) {
            vo.setAvatar(info.getAvatar());
        } else {
            vo.setAvatar("https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/" + info.getAvatar());
        }

        return vo;
    }

    /**
     * 赞了我的
     *
     * @param username
     * @return
     */
    @RequestMapping("/praiseNotice")
    @ResponseBody
    public ForumResult praiseMe(@RequestParam("username") String username) {
        //数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        UserInfo info = userInfoService.getUserInfoByName(username);
        if (info == null) {
            return new ForumResult(400, "用户不存在", null);
        }

        List<CommentLike> likes = commentLikeService.selectCommentLikeByUsername(username);
        if (likes.size() == 0) {
            return new ForumResult(200, "查询成功", null);
        } else {
            List<CommentLikeVO> res = new ArrayList<>();
            for (CommentLike commentLike : likes) {
                res.add(convertCommentLikeToVO(commentLike));
            }
            return new ForumResult(200, "查询成功", res);
        }
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
        vo.setUsername(commentLike.getLikeName());
        vo.setUid(commentLike.getLikeId());

        UserInfo info = userInfoService.getUserInfoByName(commentLike.getLikeName());
        if (info.getAvatar().contains("https")) {
            vo.setAvatar(info.getAvatar());
        } else {
            vo.setAvatar("https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/" + info.getAvatar());
        }

        vo.setQuestionId(commentLike.getQuestionId());
        Question question = questionService.selectByPrimaryKey(commentLike.getQuestionId());
        vo.setTitle(question.getTitle());

        Comment comment = commentService.selectCommentByPrimaryKey(commentLike.getCommentId());
        String text = Html2Text(comment.getComment());
        if (text.length() > 20) {
            vo.setContent(text.substring(0, 20));
        } else {
            vo.setContent(text);
        }
        vo.setTime(commentLike.getTime());

        return vo;
    }

    //从html中提取纯文本
    private String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        java.util.regex.Matcher m_script;
        Pattern p_style;
        java.util.regex.Matcher m_style;
        Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        //剔除空格行
        textStr = textStr.replaceAll("[ ]+", " ");
        textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return textStr;// 返回文本字符串
    }
}
