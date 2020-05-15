package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.Question;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题Mapper
 */
@Component
public interface QuestionMapper {

    /**
     * 获取所有的问题信息
     * @return
     */
    List<Question> getAllQuestions();

    /**
     * 根据关键字查询问题信息
     * @param keyword
     * @return
     */
    List<Question> getAllQuestionsWithKeyword(String keyword);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    Question selectByPrimaryKey(Integer id);

    /**
     * 根据问题Id查询标签名
     * @param questionId
     * @return
     */
    String queryTagByQuestionId(Integer questionId);

    /**
     * 根据浏览量查询文章信息
     * @return
     */
    List<Question> selectQuestionInfoByViewCount();

    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    /**
     * 更新阅读量
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Question record);

    /**
     * 编辑问题信息
     * @param record
     * @return
     */
    int updateQuestionInfoByIdSelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    /**
     * 更新likeCount
     * @param likeCount
     * @param questionId
     * @return
     */
    int updateLikeCountById(Integer likeCount,Integer questionId);

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