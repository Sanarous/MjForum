package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.QuestionEditMapper;
import cn.bestzuo.zuoforum.pojo.QuestionEdit;
import cn.bestzuo.zuoforum.service.QuestionEditService;
import cn.bestzuo.zuoforum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问题置顶与加精Service
 *
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/6 18:31
 */
@Service
public  class QuestionEditServiceImpl implements QuestionEditService {

    @Autowired
    private QuestionEditMapper questionEditMapper;

    /**
     * 查询问题的置顶和加精信息
     * @param id 问题置顶和加精信息ID
     * @return
     */
    @Override
    public QuestionEdit selectQuestionEditById(Integer id) {
        return questionEditMapper.queryQuestionEditInfoByQuestionId(id);
    }

    /**
     * 获取所有精品问题
     * @return 问题置顶和加精信息
     */
    @Override
    public List<QuestionEdit> queryAllJingQuestions() {
        return questionEditMapper.queryAllJingQuestions();
    }
}
