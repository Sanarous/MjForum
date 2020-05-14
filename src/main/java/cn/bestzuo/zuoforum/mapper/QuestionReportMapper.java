package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.QuestionReport;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题举报Mapper
 *
 * @author zuoxiang
 * @date 2020/5/5 11:50
 */
@Component
public interface QuestionReportMapper {

    /**
     * 根据举报ID查询举报信息
     * @param id
     * @return
     */
    QuestionReport queryReportById(Integer id);

    /**
     * 查询所有未处理的举报信息
     * @return
     */
    List<QuestionReport> queryAllNotProcessReport();

    /**
     * 查询所有处理过的举报信息
     * @return
     */
    List<QuestionReport> queryAllProcessReport();

    /**
     * 查询带关键字的举报信息
     * @param id
     * @return
     */
    List<QuestionReport> queryAllReportWithKeyword(Integer id);

    /**
     * 根据举报用户ID和问题ID查询数据库中信息
     * @param userId
     * @param questionId
     * @return
     */
    QuestionReport selectReportByUserIdAndQuestionId(Integer userId,Integer questionId);

    /**
     * 新增举报信息
     * @param questionReport
     * @return
     */
    int insertQuestionReport(QuestionReport questionReport);

    /**
     * 更新处理结果
     * @param id
     * @return
     */
    int updateQuestionReportStatusById(Integer id,String result);
}
