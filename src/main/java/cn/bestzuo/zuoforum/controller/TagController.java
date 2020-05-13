package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.vo.QuestionVO;
import cn.bestzuo.zuoforum.pojo.vo.UserIndexQuestionVO;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.service.QuestionTagService;
import cn.bestzuo.zuoforum.service.TagService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签管理Controller
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/1 22:27
 */
@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private QuestionTagService questionTagService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserInfoService userInfoService;


    /**
     * 根据标签名获取该标签对应的所有问题信息
     * @param tagName
     * @return
     */
    @GetMapping("/getQuestionByTag")
    @ResponseBody
    public ForumResult queryQuestionByTagName(@RequestParam(defaultValue = "1", value = "currPage") Integer currPage,
                                              @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize,
                                              @RequestParam("tagName") String tagName){
        if(tagName == null || "".equals(tagName) || tagName.length() == 0){
            return new ForumResult(400,"标签名不能为空",null);
        }

        Integer tagId = tagService.selectTagIdByTagName(tagName);
        if(tagId == null) return new ForumResult(400,"该标签不存在",null);

        PageHelper.startPage(currPage, pageSize);
        //根据tagId查询对应的所有问题信息，并包装成questionVO转发到前端
        List<Integer> quesIdList = questionTagService.selectQuestionIdByTagId(tagId);
        List<QuestionVO> res = new ArrayList<>();
        for(Integer i : quesIdList){
            Question question = questionService.selectByPrimaryKey(i);
            res.add(convertQuestionToVO(question));
        }

        PageInfo<QuestionVO> pageInfo = new PageInfo<>(res);
        if(res.size() == 0){
            return new ForumResult(500,"该标签下没有相关的问题",null);
        }
        return ForumResult.ok(res);
    }

    /**
     * 查询数据库中条数
     * @return
     */
    @GetMapping("/getQuestionByTagTotalCount")
    @ResponseBody
    public ForumResult getTotalCount(@RequestParam("tagName") String tagName){
        if(tagName == null || tagName == "" || tagName.length() == 0){
            return new ForumResult(400,"标签名不能为空",null);
        }

        Integer tagId = tagService.selectTagIdByTagName(tagName);
        if(tagId == null) return new ForumResult(400,"该标签不存在",null);

        return new ForumResult(200,"查询成功",questionTagService.selectQuestionIdByTagId(tagId).size());
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

        return vo;
    }
}
