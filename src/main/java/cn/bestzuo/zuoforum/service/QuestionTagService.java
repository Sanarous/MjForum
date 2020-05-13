package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.QuestionTag;

import java.util.List;

public interface QuestionTagService {

    /**
     * 添加问题ID和标签的对应关系
     * @param questionTag
     * @return
     */
    int insertQuestionTag(QuestionTag questionTag);

    /**
     * 查询引用最多的tagId
     * @return
     */
    List<Integer> selectMostReferTag();

    /**
     * 根据问题ID查询对应的标签信息集
     * @param questionId
     * @return
     */
    List<Integer> selectTagByQuestionId(Integer questionId);

    /**
     * 根据tagId查询questionId
     * @param tagId
     * @return
     */
    List<Integer> selectQuestionIdByTagId(Integer tagId);
}
