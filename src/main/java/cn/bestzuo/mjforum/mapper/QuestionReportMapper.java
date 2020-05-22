package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.QuestionReport;
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
     *
     * @param id 主键
     * @return 举报信息
     */
    QuestionReport queryReportById(Integer id);

    /**
     * 查询所有未处理的举报信息
     *
     * @return 举报信息
     */
    List<QuestionReport> queryAllNotProcessReport();

    /**
     * 查询所有处理过的举报信息
     *
     * @return 举报信息
     */
    List<QuestionReport> queryAllProcessReport();

    /**
     * 查询带关键字的举报信息
     *
     * @param id 主键
     * @return 举报信息
     */
    List<QuestionReport> queryAllReportWithKeyword(Integer id);

    /**
     * 根据举报用户ID和问题ID查询数据库中信息
     *
     * @param userId     用户ID
     * @param questionId 问题ID
     * @return 举报信息
     */
    QuestionReport selectReportByUserIdAndQuestionId(Integer userId, Integer questionId);

    /**
     * 新增举报信息
     *
     * @param questionReport 举报信息
     * @return 更新行数
     */
    int insertQuestionReport(QuestionReport questionReport);

    /**
     * 更新处理结果
     *
     * @param id 主键
     * @return 更新行数
     */
    int updateQuestionReportStatusById(Integer id, String result);
}
