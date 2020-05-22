package cn.bestzuo.mjforum.mapper;


import cn.bestzuo.mjforum.pojo.QuestionTag;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题与标签Mapper
 */
@Component
public interface QuestionTagMapper {

    /**
     * 添加问题ID和标签的对应关系
     *
     * @param questionTag 问题标签
     * @return 更新行数
     */
    int insertQuestionTag(QuestionTag questionTag);


    /**
     * 查询引用最多的tagId
     *
     * @return tagID集合
     */
    List<Integer> selectMostReferTag();

    /**
     * 根据问题ID查询对应的标签ID集
     *
     * @param questionId 问题ID
     * @return tagId
     */
    List<Integer> selectTagByQuestionId(Integer questionId);

    /**
     * 删除问题和标签对应的关系
     *
     * @param questionId 问题ID
     * @param tagId      标签ID
     * @return 更新行数
     */
    int deleteQuestionAndTagInfo(Integer questionId, Integer tagId);

    /**
     * 根据标签ID查询问题ID
     *
     * @param tagId 标签ID
     * @return 问题ID
     */
    List<Integer> selectQuestionIdByTagId(Integer tagId);
}
