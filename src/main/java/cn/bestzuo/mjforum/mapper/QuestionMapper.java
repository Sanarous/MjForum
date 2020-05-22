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
     * @return 问题信息
     */
    List<Question> getAllQuestions();

    /**
     * 获取所有置顶问题
     * @return 问题信息
     */
    List<Question> getDingQuestions();

    /**
     * 获取所有加精问题
     * @return 问题信息
     */
    List<Question> getJingQuestions();

    /**
     * 根据关键字查询问题信息
     * @param keyword 关键词指标题中的字
     * @return 问题信息
     */
    List<Question> getAllQuestionsWithKeyword(String keyword);

    /**
     * 根据主键查询
     * @param id 问题ID
     * @return 问题信息
     */
    Question selectByPrimaryKey(Integer id);

    /**
     * 根据问题Id查询标签名
     * @param questionId 问题ID
     * @return 标签名
     */
    String queryTagByQuestionId(Integer questionId);

    /**
     * 根据浏览量查询文章信息
     * @return 问题信息
     */
    List<Question> selectQuestionInfoByViewCount();

    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    /**
     * 更新阅读量
     * @param record 问题信息
     * @return 更新行数
     */
    int updateByPrimaryKeySelective(Question record);

    /**
     * 编辑问题信息
     * @param record 问题信息
     * @return 更新行数
     */
    int updateQuestionInfoByIdSelective(Question record);

    int updateByPrimaryKeyWithBLOBs(Question record);

    int updateByPrimaryKey(Question record);

    /**
     * 更新likeCount
     * @param likeCount  收藏数
     * @param questionId  问题ID
     * @return  更新行数
     */
    int updateLikeCountById(Integer likeCount, Integer questionId);

    /**
     * 推荐
     * @return  问题信息
     */
    List<Question> getAllQuestionsByViewCount();

    /**
     * 热门
     * @return  问题信息
     */
    List<Question> getAllQuestionsByCommentCount();

    /**
     * 消灭0回复
     * @return  问题信息
     */
    List<Question> getAllQuestionsByZeroComment();

    /**
     * 根据发布者查询发布的问题
     * @param publisherId 用户ID
     * @return  问题信息
     */
    List<Question> getAllQuestionsByPublisherId(Integer publisherId);

    /**
     * 获取我的最热问题
     * @param publisherId 用户ID
     * @return  问题信息
     */
    List<Question> selectMyHotQuestions(Integer publisherId);

    /**
     * 查询论坛推荐问题
     * @return  问题信息
     */
    List<Question> selectForumRecommendQuestions();

    /**
     * 更新置顶状态
     * @param isDing 置顶状态  1-已置顶 0 - 未置顶
     * @param questionId 问题ID
     * @return 更新行数
     */
    int updateDingStatusByQuestionId(int isDing, int questionId);

    /**
     * 更新加精状态
     * @param isJing  加精状态 1-已加精 0-未加精
     * @param questionId 问题ID
     * @return  更新行数
     */
    int updateJingStatusByQuestionId(int isJing, int questionId);
}