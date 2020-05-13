package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.common.ForumResult;

/**
 * 举报管理Service
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/5 13:51
 */
public interface QuestionReportService {

    ForumResult processReport(String username, String rUsername, Integer rQuestionId, String reason);
}
