package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.mapper.TagMapper;
import cn.bestzuo.mjforum.pojo.Tags;
import cn.bestzuo.mjforum.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 标签管理Service
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 查询数据库中所有的标签信息
     * @return 标签
     */
    @Override
    public List<Tags> queryAllTags() {
        return tagMapper.queryAllTags();
    }

    /**
     * 查询所有默认标签的信息
     *
     * @return tag实体
     */
    @Override
    public List<Tags> queryAllTagsForOrigin() {
        return tagMapper.queryAllTagsForOrigin();
    }

    /**
     * 根据标签ID查询标签信息
     * @param tagId  标签ID
     * @return  标签信息
     */
    @Override
    public Tags selectTagByTagId(Integer tagId) {
        return tagMapper.selectTagByTagId(tagId);
    }

    /**
     * 新增标签信息
     */
    @Override
    @Transactional
    public int insertNewTag(Tags tag) {
        return tagMapper.insertNewTag(tag);
    }

    /**
     * 新增标签和问题的对应关系
     * @param tagId  标签ID
     * @param questionId  问题ID
     */
    @Override
    @Transactional
    public void insertQuestionAndTag(Integer tagId, Integer questionId) {
        tagMapper.insertQuestionAndTag(tagId, questionId);
    }

    /**
     * 根据ID查询标签名
     * @param tagId  标签ID
     * @return  标签名
     */
    @Override
    public String selectTagNameByTagId(Integer tagId) {
        return tagMapper.selectTagNameByTagId(tagId);
    }

    /**
     * 根据问题ID查询标签ID
     * @param questionId  问题ID
     * @return  标签ID
     */
    @Override
    public List<Integer> selectTagIDsByQuestionId(Integer questionId) {
        return tagMapper.selectTagIDsByQuestionId(questionId);
    }

    /**
     * 根据标签ID查询对应的问题ID
     * @param tagId  标签ID
     * @return  问题ID
     */
    @Override
    public List<Integer> selectQuestionIdsByTagId(Integer tagId) {
        return tagMapper.selectQuestionIdsByTagId(tagId);
    }

    /**
     * 根据tagName查询tagId
     * @param tagName 标签名
     * @return 标签ID
     */
    @Override
    public Integer selectTagIdByTagName(String tagName) {
        return tagMapper.selectTagIdByTagName(tagName);
    }
}
