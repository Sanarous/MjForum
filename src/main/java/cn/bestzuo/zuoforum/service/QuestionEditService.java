package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.mapper.QuestionEditMapper;
import cn.bestzuo.zuoforum.pojo.QuestionEdit;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/6 18:30
 */
public interface QuestionEditService {

    /**
     * 查询问题置顶和加精信息
     * @param id
     * @return
     */
    QuestionEdit selectQuestionEditById(Integer id);

    /**
     * 获取所有精品问题
     * @return
     */
    List<QuestionEdit> queryAllJingQuestions();
}
