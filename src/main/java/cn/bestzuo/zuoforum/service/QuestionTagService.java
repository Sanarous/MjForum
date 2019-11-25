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
}
