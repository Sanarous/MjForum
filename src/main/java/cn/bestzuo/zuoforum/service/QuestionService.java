package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.Question;

import java.util.List;

public interface QuestionService {

    /**
     * 新增问题
     * @param question
     */
    void insertQuestion(Question question);

    /**
     * 查询所有问题信息
     * @return
     */
    List<Question> queryAllQuestions();

    /**
     * 推荐
     * @return
     */
    List<Question> getAllQuestionsByViewCount();

    /**
     * 热门
     * @return
     */
    List<Question> getAllQuestionsByCommentCount();

    /**
     * 消灭0回复
     * @return
     */
    List<Question> getAllQuestionsByZeroComment();

    /**
     * 根据主键查询问题
     * @param id
     * @return
     */
    Question selectByPrimaryKey(Integer id);

    /**
     * 根据主键有选择的更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Question record);

    /**
     * 根据浏览量查询文章信息
     * @return
     */
    List<Question> selectQuestionInfoByViewCount();

    /**
     * 根据问题Id查询标签信息
     * @param questionId
     * @return
     */
    String queryTagByQuestionId(Integer questionId);

    /**
     * 根据发布者查询发布的问题
     * @param publisher
     * @return
     */
    List<Question> getAllQuestionsByPublisher(String publisher);

    /**
     * 获取我的最热问题
     * @param username
     * @return
     */
    List<Question> selectMyHotQuestions(String username);

    /**
     * 查询论坛推荐问题
     * @return
     */
    List<Question> selectForumRecommendQuestions();
}
