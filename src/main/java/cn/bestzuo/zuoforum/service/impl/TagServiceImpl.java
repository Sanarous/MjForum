package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.TagMapper;
import cn.bestzuo.zuoforum.pojo.Tags;
import cn.bestzuo.zuoforum.service.TagService;
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
     * 查询所有标签信息
     *
     * @return tag实体
     */
    @Override
    public List<Tags> queryAllTags() {
        return tagMapper.queryAllTags();
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
     * @param tagId
     * @param questionId
     */
    @Override
    @Transactional
    public void insertQuestionAndTag(Integer tagId, Integer questionId) {
        tagMapper.insertQuestionAndTag(tagId, questionId);
    }

    /**
     * 根据ID查询标签名
     * @param tagId
     * @return
     */
    @Override
    public String selectTagNameByTagId(Integer tagId) {
        return tagMapper.selectTagNameByTagId(tagId);
    }

    /**
     * 根据问题ID查询标签ID
     * @param questionId
     * @return
     */
    @Override
    public List<Integer> selectTagIDsByQuestionId(Integer questionId) {
        return tagMapper.selectTagIDsByQuestionId(questionId);
    }

    /**
     * 根据标签ID查询对应的问题ID
     * @param tagId
     * @return
     */
    @Override
    public List<Integer> selectQuestionIdsByTagId(Integer tagId) {
        return tagMapper.selectQuestionIdsByTagId(tagId);
    }
}
