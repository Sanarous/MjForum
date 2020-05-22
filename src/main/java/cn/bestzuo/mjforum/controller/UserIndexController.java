package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.common.LayuiFlowResult;
import cn.bestzuo.mjforum.pojo.*;
import cn.bestzuo.mjforum.pojo.vo.FollowVO;
import cn.bestzuo.mjforum.pojo.vo.UserIndexCommentsVO;
import cn.bestzuo.mjforum.pojo.vo.UserIndexInfoVO;
import cn.bestzuo.mjforum.pojo.vo.UserIndexQuestionVO;
import cn.bestzuo.mjforum.service.*;
import cn.bestzuo.mjforum.util.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户主页Controller
 *
 * @author zuoxiang
 * @date 2019/11/22
 */
@Controller
public class UserIndexController {

    private final CollectionService collectionService;

    private final UserInfoService userInfoService;

    private final QuestionService questionService;

    private final CommentService commentService;

    private final FollowService followService;

    private final CommentLikeService commentLikeService;

    private final UserRateService userRateService;

    @Autowired
    public UserIndexController(CollectionService collectionService, UserInfoService userInfoService, QuestionService questionService, CommentService commentService, FollowService followService, CommentLikeService commentLikeService, UserRateService userRateService) {
        this.collectionService = collectionService;
        this.userInfoService = userInfoService;
        this.questionService = questionService;
        this.commentService = commentService;
        this.followService = followService;
        this.commentLikeService = commentLikeService;
        this.userRateService = userRateService;
    }

    /**
     * 查询对应的用户名
     *
     * @param token  用户ID
     * @return 用户名
     */
    @GetMapping("/user/{token}")
    public String userIndex(@PathVariable(value = "token") String token, Model model) {
        if (token == null) {
            return "404";
        }
        //根据用户ID查询对应的用户信息
        int uid = Integer.parseInt(token);
        //查询用户ID
        UserInfo userInfo = userInfoService.selectUserInfoByUid(uid);
        if (userInfo == null) return "404";
        //保存用户信息
        model.addAttribute("username", userInfo.getUsername());
        return "user/user";
    }


    /**
     * 获取首页用户信息
     *
     * @param username  用户名
     * @return 包装结果
     */
    @GetMapping("/getUserIndexInfo")
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
        vo.setAvatar(userInfoByName.getAvatar());
        vo.setSex(userInfoByName.getSex());
        UserRate userRate = userRateService.selectRateById(userInfoByName.getUId());
        vo.setRate(userRate == null ? 0 : userRate.getRate());

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
     * @return 包装结果
     */
    @GetMapping("/getMyQuestions")
    @ResponseBody
    public LayuiFlowResult getMyQuestions(@RequestParam("page") Integer page,
                                          @RequestParam("username") String username) {
        //后端校验数据
        if (StringUtils.isEmpty(username)) {
            return new LayuiFlowResult(400, "用户名不能为空", null,0);
        }

        //后台查询数据库信息
        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new LayuiFlowResult(400, "用户不存在", null,0);
        }

        //查询问题信息
        PageHelper.startPage(page,5);
        List<Question> questions = questionService.getAllQuestionsByPublisher(username);
        PageInfo<Question> pageInfo = new PageInfo<>(questions);


        if (pageInfo.getList().size() == 0) {
            return new LayuiFlowResult(200, "查询成功", null,0);
        } else {
            List<UserIndexQuestionVO> res = new ArrayList<>();
            for (Question question : pageInfo.getList()) {
                res.add(convertQuestionToVO(question));
            }
            return new LayuiFlowResult(200, "查询成功", res,pageInfo.getPages());
        }
    }

    /**
     * 将Question信息转换成前端VO
     *
     * @param question  问题信息
     * @return 前端VO类
     */
    private UserIndexQuestionVO convertQuestionToVO(Question question) {
        UserIndexQuestionVO vo = new UserIndexQuestionVO();
        vo.setId(question.getId());
        vo.setTitle(question.getTitle());
        vo.setCommentCount(question.getCommentCount());
        vo.setLikeCount(question.getLikeCount());
        vo.setGmtCreate(question.getGmtCreate());
        vo.setViewCount(question.getViewCount());

        String text = CommonUtils.Html2Text(question.getDescription());
        vo.setDescription(text.length() > 20 ? text.substring(0, 20) + "..." : text);
        return vo;
    }

    /**
     * 获取我的评论信息
     *
     * @return 包装结果
     */
    @GetMapping("/getMyCommentInfo")
    @ResponseBody
    public LayuiFlowResult getMyComments(@RequestParam("page")Integer page,
                                     @RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new LayuiFlowResult(400, "用户未登录", null,0);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new LayuiFlowResult(400, "用户信息不存在", null,0);
        }

        //查询数据库，封装成前端VO
        PageHelper.startPage(page,5);
        List<Comment> comments = commentService.selectCommentsByUname(username);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);

        if (pageInfo.getList().size() == 0) {
            return new LayuiFlowResult(200, "查询成功", null,0);
        } else {
            List<UserIndexCommentsVO> res = new ArrayList<>();
            for (Comment c : pageInfo.getList()) {
                res.add(convertCommentToVO(c));
            }
            return new LayuiFlowResult(200, "查询成功", res,pageInfo.getPages());
        }
    }

    /**
     * 将评论信息转换成前端VO
     *
     * @param comment  评论信息
     */
    private UserIndexCommentsVO convertCommentToVO(Comment comment) {
        UserIndexCommentsVO vo = new UserIndexCommentsVO();
        vo.setId(comment.getCId());
        UserInfo userInfo = userInfoService.selectUserInfoByUid(comment.getUid());
        vo.setAvatar(userInfo.getAvatar());
        vo.setUsername(userInfo.getUsername());
        vo.setTime(comment.getTime());
        vo.setQuestionId(comment.getQuestionId());

        //设置评论内容
        String text = CommonUtils.Html2Text((comment.getComment()));
        vo.setComment(text.length() > 20 ? text.substring(0, 20) + "..." : text);

        Question question = questionService.selectByPrimaryKey(comment.getQuestionId());
        vo.setTitle(question.getTitle().length() > 20 ? question.getTitle().substring(0, 20) + "..." : question.getTitle());

        vo.setViewCount(question.getViewCount());

        //设置问题主体内容
        String contents = CommonUtils.Html2Text(question.getDescription());
        vo.setContents(contents.length() > 20 ? contents.substring(0, 20) + "..." : text);

        vo.setViewCount(question.getViewCount());
        vo.setCommentCount(question.getCommentCount());
        vo.setLikeCount(question.getLikeCount());

        return vo;
    }

    /**
     * 我的收藏信息
     *
     * @param username  用户名
     * @return 包装结果
     */
    @GetMapping("/getMyCollection")
    @ResponseBody
    public LayuiFlowResult getMyCollections(@RequestParam("page") Integer page,
                                        @RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new LayuiFlowResult(400, "用户未登录", null,0);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new LayuiFlowResult(400, "用户信息不存在", null,0);
        }


        PageHelper.startPage(page,5);
        List<Collection> collections = collectionService.selectCollectionInfoByUid(userInfoByName.getUId());
        PageInfo<Collection> pageInfo = new PageInfo<>(collections);

        if (pageInfo.getList().size() == 0) {
            return new LayuiFlowResult(200, "查询成功", null,0);
        } else {
            List<UserIndexQuestionVO> res = new ArrayList<>();
            for (Collection c : pageInfo.getList()) {
                res.add(convertQuestionToVO(questionService.selectByPrimaryKey(c.getQuestionId())));
            }
            Collections.reverse(res);
            return new LayuiFlowResult(200, "查询成功", res,pageInfo.getPages());
        }
    }

    /**
     * 获取我的热门问题
     *
     * @param username 用户名
     * @return 包装结果
     */
    @GetMapping("/getMyHotQuestions")
    @ResponseBody
    public LayuiFlowResult getMyHotQuestion(@RequestParam("page") Integer page,
                                        @RequestParam("username") String username) {
        //后端数据校验
        if (StringUtils.isEmpty(username)) {
            return new LayuiFlowResult(400, "用户未登录", null,0);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(username);
        if (userInfoByName == null) {
            return new LayuiFlowResult(400, "用户信息不存在", null,0);
        }

        PageHelper.startPage(page,5);
        List<Question> questions = questionService.selectMyHotQuestions(username);
        PageInfo<Question> pageInfo = new PageInfo<>(questions);

        if (pageInfo.getList().size() == 0) {
            return new LayuiFlowResult(200, "查询成功", null,0);
        } else {
            List<UserIndexQuestionVO> res = new ArrayList<>();
            for (Question question : pageInfo.getList()) {
                res.add(convertQuestionToVO(question));
            }
            return new LayuiFlowResult(200, "查询成功", res,pageInfo.getPages());
        }
    }

    /**
     * 关注信息跳转
     *
     * @return 页面
     */
    @GetMapping("/myfollow")
    public String index(@RequestParam(value = "choose", defaultValue = "follow") String choose, Model model) {
        model.addAttribute("choose", choose);
        return "user/follower";
    }

    /**
     * 获取我的关注人信息
     *
     * @param username  用户名
     * @return 包装结果
     */
    @GetMapping("/getFollowInfo")
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
     * @param follow  关注信息
     * @return 前端VO
     */
    private FollowVO convertFollowToVO(Follow follow) {
        FollowVO vo = new FollowVO();
        vo.setId(follow.getUserId());

        //头像
        UserInfo info = userInfoService.selectUserInfoByUid(follow.getFollowId());
        vo.setUsername(info.getUsername());
        vo.setAvatar(info.getAvatar());

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
     * @return 包装结果
     */
    @GetMapping("/getFansInfo")
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
     * @return 前端VO
     */
    private FollowVO convertFansToVO(Follow follow) {
        FollowVO vo = new FollowVO();
        vo.setId(follow.getFollowId());

        //头像
        UserInfo info = userInfoService.selectUserInfoByUid(follow.getFollowId());
        vo.setUsername(info.getUsername());
        vo.setAvatar(info.getAvatar());

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
