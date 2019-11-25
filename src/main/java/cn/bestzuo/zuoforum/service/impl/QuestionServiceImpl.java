package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.QuestionMapper;
import cn.bestzuo.zuoforum.mapper.QuestionTagMapper;
import cn.bestzuo.zuoforum.mapper.TagMapper;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.QuestionTag;
import cn.bestzuo.zuoforum.pojo.Tags;
import cn.bestzuo.zuoforum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionTagMapper questionTagMapper;

    @Autowired
    private TagMapper tagMapper;

    /**
     * 查询所有问题信息
     *
     * @return
     */
    @Override
    public List<Question> queryAllQuestions() {
        return questionMapper.getAllQuestions();
    }


    /**
     * 推荐
     * @return
     */
    @Override
    public List<Question> getAllQuestionsByViewCount() {
        return questionMapper.getAllQuestionsByViewCount();
    }

    /**
     * 热门
     * @return
     */
    @Override
    public List<Question> getAllQuestionsByCommentCount() {
        return questionMapper.getAllQuestionsByCommentCount();
    }

    /**
     * 消灭0回复
     * @return
     */
    @Override
    public List<Question> getAllQuestionsByZeroComment() {
        return questionMapper.getAllQuestionsByZeroComment();
    }

    /**
     * 新增问题，需要插入以下数据库：
     * 1. tb_question      //问题表
     * 2. tb_question_tag  //问题和标签的关系
     * 3. tb_tags          //标签表
     * 4. tb_category_tag   //标签分类和标签的关系
     *
     * @param question
     */
    @Override
    @Transactional
    public void insertQuestion(Question question) {
        //插入问题，返回主键
        questionMapper.insertSelective(question);

        //对应的问题ID
        int questionId = question.getId();

        //插入标签
        List<Tags> tagsList = tagMapper.queryAllTags();

        //问题中对应的标签
        String s = question.getTag();

        System.out.println("测试结果是:" + s.indexOf(","));

        if (s.indexOf(",") > 0) {
            //存在多个标签
            //拆解标签
            String[] tags = s.split(",");

            List<String> tagList = new ArrayList<>();
            for (Tags t : tagsList) {
                tagList.add(t.getTagsName());
            }

            Map<String, Integer> map = new LinkedHashMap<>();
            for (String t : tags) {
                if (!tagList.contains(t)) {
                    map.put(t, 6);
                }
            }

            for (Map.Entry<String, Integer> m : map.entrySet()) {
                //插入标签表
                Tags res = new Tags();
                res.setTagsName(m.getKey());
                res.setCategoryId(m.getValue());
                tagMapper.insertNewTag(res);

                //插入分类和标签的对应关系
                tagMapper.insertQuestionAndTag(res.getId(), question.getId());
            }
            //插入问题和标签的关系
            for (String t : tags) {
                //查询标签ID
                QuestionTag questionTag = new QuestionTag();
                questionTag.setTagId(tagMapper.selectTagIdByTagName(t));
                questionTag.setQuestionId(questionId);
                questionTagMapper.insertQuestionTag(questionTag);
            }

        } else {
            //只存在一个标签
            List<String> tagList = new ArrayList<>();
            for (Tags t : tagsList) {
                tagList.add(t.getTagsName());
            }
            if (!tagList.contains(s)) {
                Tags res = new Tags();
                res.setTagsName(s);
                res.setCategoryId(6);
                tagMapper.insertNewTag(res);

                //插入问题和标签的关系
                tagMapper.insertQuestionAndTag(res.getId(), question.getId());
            }

            //插入问题和标签对应的关系
            QuestionTag questionTag = new QuestionTag();
            questionTag.setTagId(tagMapper.selectTagIdByTagName(s));
            questionTag.setQuestionId(questionId);
            questionTagMapper.insertQuestionTag(questionTag);
        }
    }

    /**
     * 根据主键查询问题
     *
     * @param id
     * @return
     */
    @Override
    public Question selectByPrimaryKey(Integer id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新阅读量
     *
     * @param record
     * @return
     */
    @Override
    public int updateByPrimaryKeySelective(Question record) {
        Question question = questionMapper.selectByPrimaryKey(record.getId());
        question.setViewCount(question.getViewCount() + 1);

        return questionMapper.updateByPrimaryKeySelective(question);
    }

    /**
     * 根据问题浏览量信息查询问题信息
     * @return
     */
    @Override
    public List<Question> selectQuestionInfoByViewCount() {
        return questionMapper.selectQuestionInfoByViewCount();
    }

    /**
     * 根据问题ID查询标签
     * @param questionId
     * @return
     */
    @Override
    public String queryTagByQuestionId(Integer questionId) {
        return questionMapper.queryTagByQuestionId(questionId);
    }

    /**
     * 根据发布者查询发布的问题信息
     * @param publisher
     * @return
     */
    @Override
    public List<Question> getAllQuestionsByPublisher(String publisher) {
        return questionMapper.getAllQuestionsByPublisher(publisher);
    }

    /**
     * 获取我的最热问题
     * @param username
     * @return
     */
    @Override
    public List<Question> selectMyHotQuestions(String username) {
        return questionMapper.selectMyHotQuestions(username);
    }

    /**
     * 查询论坛推荐问题
     * @return
     */
    @Override
    public List<Question> selectForumRecommendQuestions() {
        return questionMapper.selectForumRecommendQuestions();
    }
}
