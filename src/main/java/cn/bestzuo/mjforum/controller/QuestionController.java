package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.Question;
import cn.bestzuo.mjforum.pojo.QuestionEdit;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.vo.QuestionVO;
import cn.bestzuo.mjforum.service.QuestionService;
import cn.bestzuo.mjforum.service.TagService;
import cn.bestzuo.mjforum.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    private final TagService tagService;

    @Autowired
    public QuestionController(QuestionService questionService, UserInfoService userInfoService, TagService tagService) {
        this.questionService = questionService;
        this.userInfoService = userInfoService;
        this.tagService = tagService;
    }

    /**
     * 获取所有问题信息
     *
     * @return
     */
    @GetMapping("/getAllQuestions")
    @ResponseBody
    public ForumResult getAllQuestions(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                       @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(currPage, pageSize);
        List<Question> questions = questionService.queryAllQuestionsWithCurrPage(currPage);
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
     * 推荐
     * @param currPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getAllQuestionsByViewCount")
    @ResponseBody
    public ForumResult getAllQuestionsByViewCount(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                                  @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
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
     * @param currPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getAllQuestionsByCommentCount")
    @ResponseBody
    public ForumResult getAllQuestionsByCommentCount(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                                  @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
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
     * @param currPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getAllQuestionsByZeroComment")
    @ResponseBody
    public ForumResult getAllQuestionsByZeroComment(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                                     @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
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
     * @param currPage
     * @param pageSize
     * @return
     */
    @RequestMapping("/getAllJingQuestions")
    @ResponseBody
    public ForumResult getAllJingQuestions(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                           @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
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
     * @return
     */
    private QuestionVO convertQuestionToVO(Question question) {
        QuestionVO vo = new QuestionVO();

        vo.setId(question.getId());
        vo.setTitle(question.getTitle());
        vo.setDescription(question.getDescription());

        //获取展示的标签信息
        String tags = question.getTag();
        if(tags.contains(",")){
            //说明有多个标签，默认展示第一个标签
            vo.setTag(tags.split(",")[0]);
        }else {
            //说明只有一个标签
            vo.setTag(tags);
        }

        vo.setPublisher(question.getPublisher());
        vo.setCommentCount(question.getCommentCount());
        vo.setViewCount(question.getViewCount());
        vo.setLikeCount(question.getLikeCount());
        vo.setGmtCreate(question.getGmtCreate());
        vo.setGmtModified(question.getGmtModified());

        UserInfo info = userInfoService.getUserInfoByName(question.getPublisher());
        vo.setAvatar(info.getAvatar());
        vo.setUid(info.getUId());

        //查询问题的置顶和加精情况
        QuestionEdit questionEdit = questionService.queryQuestionEditInfoByQuestionId(question.getId());
        if(questionEdit == null){
            vo.setIsDing(0);
            vo.setIsJing(0);
        }else{
            vo.setIsDing(questionEdit.getIsDing());
            vo.setIsJing(questionEdit.getIsJing());
        }

        return vo;
    }

    /**
     * 查询数据库中条数
     * @return
     */
    @GetMapping("/getTotalCount")
    @ResponseBody
    public ForumResult getTotalCount(){
        List<Question> list = questionService.queryAllQuestions();
        return new ForumResult(200,"查询成功",list.size());
    }

    /**
     * 获取精品帖的条数
     * @return
     */
    @GetMapping("/getJingCount")
    @ResponseBody
    public ForumResult getJingCount(){
        List<Question> allJingQuestions = questionService.getAllJingQuestions();
        return new ForumResult(200,"查询成功",allJingQuestions.size());
    }

    /**
     * 根据问题ID查询问题的标签
     * @return
     */
    @RequestMapping("/getQuestionTags")
    @ResponseBody
    public ForumResult getTags(@RequestParam("questionId") Integer questionId){
        //后端校验
        if(questionId == null){
            return new ForumResult(400,"",null);
        }

        String s = questionService.queryTagByQuestionId(questionId);
        if(s == null){
            return new ForumResult(200,"",null);
        }
        return new ForumResult(200,"",s);
    }
}
