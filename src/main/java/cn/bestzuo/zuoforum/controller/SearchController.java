package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.vo.QuestionVO;
import cn.bestzuo.zuoforum.pojo.vo.UserIndexQuestionVO;
import cn.bestzuo.zuoforum.service.SearchService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 简单搜索Controller
 */
@SuppressWarnings("Duplicates")
@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * 简单搜索Controller
     *
     * @param keywords
     * @return
     */
    @RequestMapping("/search")
    public String searchByKeywords(@RequestParam("keywords") String keywords, Model model) {
        List<Question> questions = searchService.searchByTitleAndContent(keywords);
        List<UserIndexQuestionVO> res = new ArrayList<>();

        if (questions.size() != 0) {
            for (Question q : questions) {
                res.add(convertQuestionToVO(q));
            }
            model.addAttribute("questions", res);
        } else {
            PageInfo<Question> pageInfo = new PageInfo<>(questions);
            model.addAttribute("questions", pageInfo.getList());
        }

        model.addAttribute("keywords", keywords);
        return "post/search";
    }

    /**
     * 将问题转换成前端VO
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
        vo.setPublisher(question.getPublisher());

        String text = Html2Text(question.getDescription());
        if (text.length() > 50) {
            vo.setDescription(text.substring(0, 50) + "......");
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
}
