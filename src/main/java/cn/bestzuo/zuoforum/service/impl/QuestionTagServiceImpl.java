package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.QuestionTagMapper;
import cn.bestzuo.zuoforum.pojo.QuestionTag;
import cn.bestzuo.zuoforum.service.QuestionTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionTagServiceImpl implements QuestionTagService {

    @Autowired
    private QuestionTagMapper questionTagMapper;

    /**
     * 新增问题和标签的对应关系
     *
     * @param questionTag
     * @return
     */
    @Override
    @Transactional
    public int insertQuestionTag(QuestionTag questionTag) {
        return questionTagMapper.insertQuestionTag(questionTag);
    }

    /**
     * 查询引用最多的tagId
     *
     * @return
     */
    @Override
    public List<Integer> selectMostReferTag() {
        return questionTagMapper.selectMostReferTag();
    }
}
