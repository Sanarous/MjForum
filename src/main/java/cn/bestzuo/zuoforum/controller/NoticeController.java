package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.common.LayuiFlowResult;
import cn.bestzuo.zuoforum.pojo.*;
import cn.bestzuo.zuoforum.pojo.vo.*;
import cn.bestzuo.zuoforum.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/notice/getCommentNotice")
    public String getCommentNotice(Model model){
        model.addAttribute("destination","comment");
        return "user/notice";
    }

    @GetMapping("/notice/getCollectionNotice")
    public String getCollectionNotice(Model model){
        model.addAttribute("destination","collection");
        return "user/notice";
    }

    @GetMapping("/notice/getFollowNotice")
    public String getFollowNotice(Model model){
        model.addAttribute("destination","follow");
        return "user/notice";
    }

    @GetMapping("/notice/getPraiseNotice")
    public String getPraiseNotice(Model model){
        model.addAttribute("destination","praise");
        return "user/notice";
    }

    @GetMapping("/notice/getMajiangNotice")
    public String getMajiangNotice(Model model){
        model.addAttribute("destination","majiang");
        return "user/notice";
    }

    @GetMapping("/notice/getReportNotice")
    public String getReportNotice(Model model){
        model.addAttribute("destination","report");
        return "user/notice";
    }
    @GetMapping("/notice/getOtherNotice")
    public String getOtherNotice(Model model){
        model.addAttribute("destination","other");
        return "user/notice";
    }



    /**
     * 获取评论通知消息
     *
     * @return
     */
    @RequestMapping("/commentNotice")
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
     * @param commentNoticeInfo
     * @return
     */
    private CommentNoticeVO convertCommentNoticeToVO(CommentNoticeInfo commentNoticeInfo) {
        CommentNoticeVO commentNoticeVO = new CommentNoticeVO();
        commentNoticeVO.setId(commentNoticeInfo.getId());

        //获取回复者头像
        UserInfo info = userInfoService.getUserInfoByName(commentNoticeInfo.getCommentName());
        commentNoticeVO.setCommentAvatar(info.getAvatar());
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
    public LayuiFlowResult collectMe(@RequestParam("page") Integer page,
                                     @RequestParam("username") String username) {
        //数据校验
        PageHelper.startPage(page, 5);
        List<Collection> collections = collectionService.selectCollectionByPublisher(username);
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
        UserInfo userInfo = userInfoService.getUserInfoByName(collection.getUsername());
        vo.setAvatar(userInfo.getAvatar());
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
        vo.setUsername(follow.getFollowName());

        UserInfo info = userInfoService.getUserInfoByName(follow.getFollowName());
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
    @RequestMapping("/praiseNotice")
    @ResponseBody
    public LayuiFlowResult praiseMe(@RequestParam("page") Integer page,
                                    @RequestParam("username") String username) {

        PageHelper.startPage(page, 5);
        List<CommentLike> likes = commentLikeService.selectCommentLikeByUsername(username);
        PageInfo<CommentLike> pageInfo = new PageInfo<>(likes);

        if (likes.size() == 0) return new LayuiFlowResult(200, "查询成功", null,0);

        List<CommentLikeVO> res = new ArrayList<>();
        for (CommentLike commentLike : pageInfo.getList()) {
            res.add(convertCommentLikeToVO(commentLike));
        }
        return new LayuiFlowResult(200, "查询成功", res,pageInfo.getPages());
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
        vo.setAvatar(info.getAvatar());

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
