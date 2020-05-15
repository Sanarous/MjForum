package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.mapper.QuestionTagMapper;
import cn.bestzuo.mjforum.pojo.QuestionTag;
import cn.bestzuo.mjforum.service.QuestionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 问题与标签的对应关系
 */
@Service
public class QuestionTagServiceImpl implements QuestionTagService {

    @Autowired
    private QuestionTagMapper questionTagMapper;

    /**
     * 新增问题和标签的对应关系
     *
     * @param questionTag 问题标签信息
     * @return 操作数据库的行数 > 0 表示新增成功
     */
    @Override
    @Transactional
    public int insertQuestionTag(QuestionTag questionTag) {
        return questionTagMapper.insertQuestionTag(questionTag);
    }

    /**
     * 查询引用最多的tagId
     *
     * @return 标签ID
     */
    @Override
    public List<Integer> selectMostReferTag() {
        return questionTagMapper.selectMostReferTag();
    }

    /**
     * 根据问题ID查询对应的标签信息集
     * @param questionId 问题ID
     * @return 标签ID
     */
    @Override
    public List<Integer> selectTagByQuestionId(Integer questionId) {
        return questionTagMapper.selectTagByQuestionId(questionId);
    }

    /**
     * 根据tagId查询questionId
     * @param tagId 标签ID
     * @return  问题ID
     */
    @Override
    public List<Integer> selectQuestionIdByTagId(Integer tagId) {
        return questionTagMapper.selectQuestionIdByTagId(tagId);
    }
}
