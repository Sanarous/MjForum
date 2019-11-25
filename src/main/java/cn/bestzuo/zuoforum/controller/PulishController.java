package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.common.ForumResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 问题发布Controller
 */
@Controller
public class PulishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String index() {
        return "user/publish";
    }

    /**
     * 发布问题
     *
     * @param title       标题
     * @param description 描述
     * @param tag         标签
     * @param request     req
     * @return 通用
     */
    @PostMapping("/publish")
    @ResponseBody
    public ForumResult publish(@RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("tag") String tag,
                               HttpServletRequest request) {
        Question ques = new Question();
        ques.setTitle(title);
        ques.setDescription(description);
        ques.setTag(tag);
        //添加发帖人信息
        String username = (String) request.getSession().getAttribute("username");
        ques.setPublisher(username);
        //发帖时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ques.setGmtCreate(sdf.format(date));
        //初始化默认修改时间
        ques.setGmtModified(sdf.format(date));
        ques.setLikeCount(0);
        ques.setViewCount(0);
        ques.setCommentCount(0);
        //标签
        ques.setTag(tag);

        //插入数据库
        questionService.insertQuestion(ques);

        return new ForumResult(200, "文章发布成功!", null);
    }
}
