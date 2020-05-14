package cn.bestzuo.zuoforum.mapper;


import cn.bestzuo.zuoforum.pojo.QuestionTag;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题与标签Mapper
 */
@Component
public interface QuestionTagMapper {

    /**
     * 添加问题ID和标签的对应关系
     * @param questionTag 问题标签
     * @return
     */
    int insertQuestionTag(QuestionTag questionTag);


    /**
     * 查询引用最多的tagId
     * @return
     */
    List<Integer> selectMostReferTag();

    /**
     * 根据问题ID查询对应的标签ID集
     * @param questionId
     * @return
     */
    List<Integer> selectTagByQuestionId(Integer questionId);

    /**
     * 删除问题和标签对应的关系
     * @param questionId
     * @param tagId
     * @return
     */
    int deleteQuestionAndTagInfo(Integer questionId,Integer tagId);

    /**
     * 根据标签ID查询问题ID
     * @param tagId
     * @return
     */
    List<Integer> selectQuestionIdByTagId(Integer tagId);
}
