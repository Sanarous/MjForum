package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.Question;
import cn.bestzuo.mjforum.pojo.QuestionEdit;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.vo.QuestionVO;
import cn.bestzuo.mjforum.service.QuestionService;
import cn.bestzuo.mjforum.service.TagService;
import cn.bestzuo.mjforum.service.UserInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 问题Controller
 *
 * @author zuoxiang
 * @date 2019/11/15
 */
@Controller
public class QuestionController {

    private final QuestionService questionService;

    private final UserInfoService userInfoService;

    @Autowired
    public QuestionController(QuestionService questionService, UserInfoService userInfoService) {
        this.questionService = questionService;
        this.userInfoService = userInfoService;
    }

    /**
     * 获取所有问题信息，按照最后评论时间进行排序
     *
     * @return 包装结果
     */
    @GetMapping("/getAllQuestions")
    @ResponseBody
    public ForumResult getAllQuestions(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                       @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize) throws ParseException {

        //经过排序后的问题信息
        List<Question> QuestionsList = questionService.queryAllQuestionsWithCurrPage(currPage);

        //对PageInfo手动分页
        Page page = new Page(currPage, pageSize);
        page.setTotal(QuestionsList.size());

        //计算当前需要显示的数据下标起始值
        int startIndex = (currPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, QuestionsList.size());
        page.addAll(QuestionsList.subList(startIndex,endIndex));

        //以Page创建PageInfo
        PageInfo<Question> pageInfo = new PageInfo<Question>(page);

        List<QuestionVO> res = new ArrayList<>();
        for (Question ques : pageInfo.getList()) {
            QuestionVO vo = convertQuestionToVO(ques);
            res.add(vo);
        }

        if (QuestionsList.size() != 0) {
            return new ForumResult(200, "查询成功!", res);
        }
        return new ForumResult(500, "问题数量为零", null);
    }

    /**
     * 推荐
     *
     * @param currPage 当前页数
     * @param pageSize 每页数量
     * @return 包装结果
     */
    @GetMapping("/getAllQuestionsByViewCount")
    @ResponseBody
    public ForumResult getAllQuestionsByViewCount(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                                  @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(currPage, pageSize);
        List<Question> questions = questionService.getAllQuestionsByViewCount();
        List<QuestionVO> res = new ArrayList<>();
        for (Question ques : questions) {
            QuestionVO vo = convertQuestionToVO(ques);
            res.add(vo);
        }
        PageInfo<QuestionVO> pageInfo = new PageInfo<>(res);
        if (questions.size() != 0) {
            return new ForumResult(200, "查询成功!", pageInfo.getList());
        }
        return new ForumResult(500, "问题数量为零", null);
    }

    /**
     * 最热
     *
     * @param currPage 当前页数
     * @param pageSize 每页多少条
     * @return 包装结果
     */
    @GetMapping("/getAllQuestionsByCommentCount")
    @ResponseBody
    public ForumResult getAllQuestionsByCommentCount(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                                     @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(currPage, pageSize);
        List<Question> questions = questionService.getAllQuestionsByCommentCount();
        List<QuestionVO> res = new ArrayList<>();
        for (Question ques : questions) {
            QuestionVO vo = convertQuestionToVO(ques);
            res.add(vo);
        }
        PageInfo<QuestionVO> pageInfo = new PageInfo<>(res);
        if (questions.size() != 0) {
            return new ForumResult(200, "查询成功!", pageInfo.getList());
        }
        return new ForumResult(500, "问题数量为零", null);
    }

    /**
     * 消灭0回复
     *
     * @param currPage 当前页数
     * @param pageSize 每页数量
     * @return 包装结果
     */
    @GetMapping("/getAllQuestionsByZeroComment")
    @ResponseBody
    public ForumResult getAllQuestionsByZeroComment(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                                    @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(currPage, pageSize);
        List<Question> questions = questionService.getAllQuestionsByZeroComment();
        List<QuestionVO> res = new ArrayList<>();
        for (Question ques : questions) {
            QuestionVO vo = convertQuestionToVO(ques);
            res.add(vo);
        }
        PageInfo<QuestionVO> pageInfo = new PageInfo<>(res);
        if (questions.size() != 0) {
            return new ForumResult(200, "查询成功!", pageInfo.getList());
        }
        return new ForumResult(500, "问题数量为零", null);
    }

    /**
     * 获取所有精品帖子
     *
     * @param currPage 当前页数
     * @param pageSize 每页多少条
     * @return 包装结果
     */
    @GetMapping("/getAllJingQuestions")
    @ResponseBody
    public ForumResult getAllJingQuestions(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                           @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(currPage, pageSize);
        List<Question> questions = questionService.getAllJingQuestions();
        List<QuestionVO> res = new ArrayList<>();
        for (Question ques : questions) {
            QuestionVO vo = convertQuestionToVO(ques);
            res.add(vo);
        }
        PageInfo<QuestionVO> pageInfo = new PageInfo<>(res);
        if (questions.size() != 0) {
            return new ForumResult(200, "查询成功!", pageInfo.getList());
        }
        return new ForumResult(500, "问题数量为零", null);
    }


    /**
     * 将Question转换给前端
     *
     * @param question 表单数据
     * @return 前端VO
     */
    private QuestionVO convertQuestionToVO(Question question) {
        QuestionVO vo = new QuestionVO();

        vo.setId(question.getId());
        vo.setTitle(question.getTitle());
        vo.setDescription(question.getDescription());

        //获取展示的标签信息
        String tags = question.getTag();
        if (tags.contains(",")) {
            //说明有多个标签，默认展示第一个标签
            vo.setTag(tags.split(",")[0]);
        } else {
            //说明只有一个标签
            vo.setTag(tags);
        }

        vo.setCommentCount(question.getCommentCount());
        vo.setViewCount(question.getViewCount());
        vo.setLikeCount(question.getLikeCount());
        vo.setGmtCreate(question.getGmtCreate());
        vo.setGmtModified(question.getGmtModified());

        UserInfo info = userInfoService.selectUserInfoByUid(question.getPublisherId());
        vo.setPublisher(info.getUsername());
        vo.setAvatar(info.getAvatar());
        vo.setUid(info.getUId());

        //查询问题的置顶和加精情况
        vo.setIsJing(question.getIsJing());
        vo.setIsDing(question.getIsDing());

        return vo;
    }

    /**
     * 查询数据库中条数
     *
     * @return 包装结果
     */
    @GetMapping("/getTotalCount")
    @ResponseBody
    public ForumResult getTotalCount() {
        List<Question> list = questionService.queryAllQuestions();
        return new ForumResult(200, "查询成功", list.size());
    }

    /**
     * 获取精品帖的条数
     *
     * @return 包装结果
     */
    @GetMapping("/getJingCount")
    @ResponseBody
    public ForumResult getJingCount() {
        List<Question> allJingQuestions = questionService.getAllJingQuestions();
        return new ForumResult(200, "查询成功", allJingQuestions.size());
    }

    /**
     * 根据问题ID查询问题的标签
     *
     * @return 包装结果
     */
    @GetMapping("/getQuestionTags")
    @ResponseBody
    public ForumResult getTags(@RequestParam("questionId") Integer questionId) {
        //后端校验
        if (questionId == null) {
            return new ForumResult(400, "", null);
        }

        String s = questionService.queryTagByQuestionId(questionId);
        if (s == null) {
            return new ForumResult(200, "", null);
        }
        return new ForumResult(200, "", s);
    }
}
