package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.QuestionEdit;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题加精与置顶mapper
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/3 11:00
 */
@Component
public interface QuestionEditMapper {

    /**
     * 根据问题ID查询问题信息，包括置顶和加精
     * @param questionId
     * @return
     */
    QuestionEdit queryQuestionEditInfoByQuestionId(Integer questionId);

    /**
     * 查询所有问题信息
     * @return
     */
    List<QuestionEdit> queryAllQuestionEditInfo();

    /**
     * 获取所有精品问题
     * @return
     */
    List<QuestionEdit> queryAllJingQuestions();

    /**
     * 新增问题信息
     * @param questionEdit
     * @return
     */
    int insertNewQuestionEditInfo(QuestionEdit questionEdit);

    /**
     * 更新加精状态
     * @param isJing
     * @param questionId
     * @return
     */
    int updateJingForQuestionById(int isJing,int questionId);

    /**
     * 更新置顶状态
     * @param isDing
     * @param questionId
     * @return
     */
    int updateDingForQuestionById(int isDing,int questionId);


    /**
     * 查询加精状态
     * @param questionId
     * @return
     */
    Integer queryIsJingByQuestionId(Integer questionId);

    /**
     * 查询置顶状态
     * @param questionId
     * @return
     */
    Integer queryIsDingByQuestionId(Integer questionId);
}
