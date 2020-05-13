package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.Tags;

import java.util.List;

public interface TagService {

    /**
     * 查询数据库中所有的标签信息
     * @return
     */
    List<Tags> queryAllTags();

    /**
     * 查询所有默认的的tag标签信息
     * @return  tags实体类
     */
    List<Tags> queryAllTagsForOrigin();

    /**
     * 根据标签ID查询标签信息
     * @param tagId
     * @return
     */
    Tags selectTagByTagId(Integer tagId);

    /**
     * 新增Tag信息
     */
    int insertNewTag(Tags tag);

    /**
     * 新增标签和文章的对应关系
     * @param tagId
     * @param questionId
     */
    void insertQuestionAndTag(Integer tagId,Integer questionId);


    /**
     * 根据ID查询标签名
     * @param tagId
     * @return
     */
    String selectTagNameByTagId(Integer tagId);

    /**
     * 根据问题ID查询对应的标签ID
     * @param questionId
     * @return
     */
    List<Integer> selectTagIDsByQuestionId(Integer questionId);

    /**
     * 根据标签ID查询对应的问题ID
     * @param tagId
     * @return
     */
    List<Integer> selectQuestionIdsByTagId(Integer tagId);

    /**
     * 根据tag名查询tagId
     * @param tagName
     * @return
     */
    Integer selectTagIdByTagName(String tagName);
}
