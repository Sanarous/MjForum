package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.common.ForumResult;

/**
 * 举报管理Service
 *
 * @author zuoxiang
 * @date 2020/5/5 13:51
 */
public interface QuestionReportService {

    ForumResult processReport(String username, String rUsername, Integer rQuestionId, String reason);
}
