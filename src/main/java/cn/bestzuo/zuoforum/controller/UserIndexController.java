package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.*;
import cn.bestzuo.zuoforum.pojo.vo.*;
import cn.bestzuo.zuoforum.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户主页Controller
 */
@Controller
public class UserIndexController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FollowService followService;

    @Autowired
    private CommentLikeService commentLikeService;

    /**
     * 查询对应的用户名
     *
     * @param token
     * @return
     */
    @RequestMapping("/user/{token}")
    public String userIndex(@PathVariable(value = "token") String token, Model model) {
        if (token == null) {
            return "404";
        }
        //根据用户ID查询对应的用户信息
        int uid = Integer.parseInt(token);
        //查询用户ID
        UserInfo userInfo = userInfoService.selectUserInfoByUid(uid);
        if (userInfo == null) {
            return "404";
        }
        //保存用户信息
        model.addAttribute("username", userInfo.getUsername());
        return "user/user";
    }


    /**
     * 获取首页用户信息
     *
     * @param username
     * @return
     */
    @RequestMapping("/getUserIndexInfo")
    @ResponseBody
    public ForumResult getMyIndexInfo(@RequestParam("username") String username) {
        //后端校验数据
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        //后台查询数据库信息
        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new ForumResult(400, "用户不存在", null);
        }

        //封装VO对象
        UserIndexInfoVO vo = new UserIndexInfoVO();
        vo.setId(userInfoByName.getUId());
        vo.setUsername(username);
        if (userInfoByName.getAvatar().contains("https")) {
            vo.setAvatar(userInfoByName.getAvatar());
        } else {
            vo.setAvatar("https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/" + userInfoByName.getAvatar());
        }

        try {
            //查询我关注的人数
            List<Follow> follows = followService.selectFollowByUsername(username);
            vo.setFollow(follows.size());

            //查询关注我的人数
            List<Follow> fans = followService.selectFansByUsername(username);
            vo.setFans(fans.size());

            //查询我发布的问题数
            List<Question> questions = questionService.getAllQuestionsByPublisher(username);
            vo.setQuestionNum(questions.size());

            //查询我收获的点赞数
            List<CommentLike> commentLikes = commentLikeService.selectCommentLikeByUsername(username);
            vo.setLikeCount(commentLikes.size());

            return new ForumResult(200, "查询成功", vo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "查询失败", null);
        }
    }

    /**
     * 获取我的问题信息
     *
     * @return
     */
    @RequestMapping("/getMyQuestions")
    @ResponseBody
    public ForumResult getMyQuestions(@RequestParam("username") String username) {
        //后端校验数据
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        //后台查询数据库信息
        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new ForumResult(400, "用户不存在", null);
        }

        //查询问题信息
        List<Question> questions = questionService.getAllQuestionsByPublisher(username);
        if (questions == null || questions.size() == 0) {
            return new ForumResult(200, "查询成功", null);
        } else {
            List<UserIndexQuestionVO> res = new ArrayList<>();
            for (Question question : questions) {
                res.add(convertQuestionToVO(question));
            }
            return new ForumResult(200, "查询成功", res);
        }
    }

    /**
     * 将Question信息转换成前端VO
     *
     * @param question
     * @return
     */
    private UserIndexQuestionVO convertQuestionToVO(Question question) {
        UserIndexQuestionVO vo = new UserIndexQuestionVO();
        vo.setId(question.getId());
        vo.setTitle(question.getTitle());
        vo.setCommentCount(question.getCommentCount());
        vo.setLikeCount(question.getLikeCount());
        vo.setGmtCreate(question.getGmtCreate());
        vo.setViewCount(question.getViewCount());

        String text = Html2Text(question.getDescription());
        if (text.length() > 20) {
            vo.setDescription(text.substring(0, 20) + "...");
        } else
            vo.setDescription(text);
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

    /**
     * 获取我的评论信息
     *
     * @return
     */
    @RequestMapping("/getMyCommentInfo")
    @ResponseBody
    public ForumResult getMyComments(@RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户未登录", null);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new ForumResult(400, "用户信息不存在", null);
        }

        //查询数据库，封装成前端VO
        List<Comment> comments = commentService.selectCommentsByUname(username);
        if (comments == null || comments.size() == 0) {
            return new ForumResult(200, "查询成功", null);
        } else {
            List<UserIndexCommentsVO> res = new ArrayList<>();
            for (Comment c : comments) {
                res.add(convertCommentToVO(c));
            }
            return new ForumResult(200, "查询成功", res);
        }
    }

    /**
     * 将评论信息转换成前端VO
     *
     * @param comment
     */
    private UserIndexCommentsVO convertCommentToVO(Comment comment) {
        UserIndexCommentsVO vo = new UserIndexCommentsVO();
        vo.setId(comment.getCId());
        vo.setAvatar(comment.getAvatar());
        vo.setUsername(comment.getUname());
        vo.setTime(comment.getTime());
        vo.setQuestionId(comment.getQuestionId());

        //设置评论内容
        String text = Html2Text(comment.getComment());
        if (text.length() > 20) {
            vo.setComment(text.substring(0, 20) + "...");
        } else
            vo.setComment(text);

        Question question = questionService.selectByPrimaryKey(comment.getQuestionId());
        if (question.getTitle().length() > 20) {
            vo.setTitle(question.getTitle().substring(0, 20) + "...");
        } else
            vo.setTitle(question.getTitle());

        vo.setViewCount(question.getViewCount());

        //设置问题主体内容
        String contents = Html2Text(question.getDescription());
        if (contents.length() > 20) {
            vo.setContents(contents.substring(0, 20) + "...");
        } else
            vo.setContents(text);

        vo.setViewCount(question.getViewCount());
        vo.setCommentCount(question.getCommentCount());
        vo.setLikeCount(question.getLikeCount());

        return vo;
    }

    /**
     * 我的收藏信息
     *
     * @param username
     * @return
     */
    @RequestMapping("/getMyCollection")
    @ResponseBody
    public ForumResult getMyCollections(@RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户未登录", null);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new ForumResult(400, "用户信息不存在", null);
        }

        List<Collection> collections = collectionService.selectCollectionInfoByUsername(username);
        if (collections == null || collections.size() == 0) {
            return new ForumResult(200, "查询成功", null);
        } else {
            List<UserIndexQuestionVO> res = new ArrayList<>();
            for (Collection c : collections) {
                res.add(convertQuestionToVO(questionService.selectByPrimaryKey(c.getQuestionId())));
            }
            Collections.reverse(res);
            return new ForumResult(200, "查询成功", res);
        }
    }

    /**
     * 获取我的热门问题
     *
     * @param username 用户名
     * @return
     */
    @RequestMapping("/getMyHotQuestions")
    @ResponseBody
    public ForumResult getMyHotQuestion(@RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户未登录", null);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new ForumResult(400, "用户信息不存在", null);
        }

        List<Question> questions = questionService.selectMyHotQuestions(username);
        if (questions == null || questions.size() == 0) {
            return new ForumResult(200, "查询成功", null);
        } else {
            List<UserIndexQuestionVO> res = new ArrayList<>();
            for (Question question : questions) {
                res.add(convertQuestionToVO(question));
            }
            return new ForumResult(200, "查询成功", res);
        }
    }

    /**
     * 关注信息跳转
     *
     * @return
     */
    @GetMapping("/myfollow")
    public String index(@RequestParam(value = "choose", defaultValue = "follow") String choose, Model model) {
        model.addAttribute("choose", choose);
        return "user/follower";
    }

    /**
     * 获取我的关注人信息
     *
     * @param username
     * @return
     */
    @RequestMapping("/getFollowInfo")
    @ResponseBody
    public ForumResult getFollowInfo(@RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户未登录", null);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new ForumResult(400, "用户信息不存在", null);
        }

        List<Follow> follows = followService.selectFollowByUsername(username);
        if (follows.size() != 0) {
            //将Follow转换成前端VO
            List<FollowVO> list = new ArrayList<>();
            for (Follow follow : follows) {
                list.add(convertFollowToVO(follow));
            }
            return new ForumResult(200, "查询成功", list);
        } else {
            return new ForumResult(200, "查询成功", null);
        }
    }

    /**
     * 将Follow转转成前端VO
     *
     * @param follow
     * @return
     */
    private FollowVO convertFollowToVO(Follow follow) {
        FollowVO vo = new FollowVO();
        vo.setId(follow.getUserId());
        vo.setUsername(follow.getUserName());

        //头像
        UserInfo info = userInfoService.getUserInfoByName(follow.getUserName());
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

        //关注者信息
        vo.setFollow(followService.selectFollowByUsername(info.getUsername()).size());

        //粉丝信息
        vo.setFans(followService.selectFansByUsername(info.getUsername()).size());

        //发起的问题
        vo.setQuestionNum(questionService.getAllQuestionsByPublisher(info.getUsername()).size());

        //获得的点赞总数
        vo.setLikeCount(commentLikeService.selectCommentLikeByUsername(info.getUsername()).size());

        return vo;
    }

    /**
     * 获取粉丝信息
     *
     * @param username 用户名
     * @return
     */
    @RequestMapping("/getFansInfo")
    @ResponseBody
    public ForumResult getMyFans(@RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户未登录", null);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new ForumResult(400, "用户信息不存在", null);
        }

        List<Follow> fans = followService.selectFansByUsername(username);
        if (fans.size() != 0) {
            //将Follow转换成前端VO
            List<FollowVO> list = new ArrayList<>();
            for (Follow follow : fans) {
                list.add(convertFansToVO(follow));
            }
            return new ForumResult(200, "查询成功", list);
        } else {
            return new ForumResult(200, "查询成功", null);
        }
    }

    /**
     * 将Fans转转成前端VO
     *
     * @param follow 关注者
     * @return
     */
    private FollowVO convertFansToVO(Follow follow) {
        FollowVO vo = new FollowVO();
        vo.setId(follow.getFollowId());
        vo.setUsername(follow.getFollowName());

        //头像
        UserInfo info = userInfoService.getUserInfoByName(follow.getFollowName());
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

        //关注者信息
        vo.setFollow(followService.selectFollowByUsername(info.getUsername()).size());

        //粉丝信息
        vo.setFans(followService.selectFansByUsername(info.getUsername()).size());

        //发起的问题
        vo.setQuestionNum(questionService.getAllQuestionsByPublisher(info.getUsername()).size());

        //获得的点赞总数
        vo.setLikeCount(commentLikeService.selectCommentLikeByUsername(info.getUsername()).size());

        return vo;
    }

}
