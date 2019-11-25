package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.vo.QuestionVO;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.service.UserInfoService;
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
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserInfoService userInfoService;

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
        List<Question> questions = questionService.queryAllQuestions();
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
        vo.setTag(question.getTag());
        vo.setPublisher(question.getPublisher());
        vo.setCommentCount(question.getCommentCount());
        vo.setViewCount(question.getViewCount());
        vo.setLikeCount(question.getLikeCount());
        vo.setGmtCreate(question.getGmtCreate());
        vo.setGmtModified(question.getGmtModified());

        UserInfo info = userInfoService.getUserInfoByName(question.getPublisher());
        if(info.getAvatar().contains("https")){
            vo.setAvatar(info.getAvatar());
        }else{
            String path = "https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/";
            vo.setAvatar(path + info.getAvatar());
        }
        vo.setUid(info.getUId());

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
