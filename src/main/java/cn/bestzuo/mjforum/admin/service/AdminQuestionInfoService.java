package cn.bestzuo.mjforum.admin.service;

import cn.bestzuo.mjforum.admin.pojo.QuestionInfo;
import cn.bestzuo.mjforum.admin.pojo.QuestionReportVO;
import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.mapper.QuestionMapper;
import cn.bestzuo.mjforum.mapper.QuestionReportMapper;
import cn.bestzuo.mjforum.mapper.UserInfoMapper;
import cn.bestzuo.mjforum.mapper.UserRateMapper;
import cn.bestzuo.mjforum.pojo.Question;
import cn.bestzuo.mjforum.pojo.QuestionReport;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.UserRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/3 20:02
 */
@Service
public class AdminQuestionInfoService {

    private final QuestionMapper questionMapper;

    private final UserInfoMapper userInfoMapper;

    private final QuestionReportMapper questionReportMapper;

    private final UserRateMapper userRateMapper;

    @Autowired
    public AdminQuestionInfoService(QuestionMapper questionMapper, UserInfoMapper userInfoMapper, QuestionReportMapper questionReportMapper, UserRateMapper userRateMapper) {
        this.questionMapper = questionMapper;
        this.userInfoMapper = userInfoMapper;
        this.questionReportMapper = questionReportMapper;
        this.userRateMapper = userRateMapper;
    }

    /**
     * 查询所有问题，并转换成前端VO
     *
     * @return
     */
    public List<QuestionInfo> queryAllQuestionInfoWithKeyword(String keyword) {
        List<QuestionInfo> res = new ArrayList<>();
        List<Question> questions = questionMapper.getAllQuestionsWithKeyword(keyword);

        if (questions.size() != 0) {
            for (Question q : questions) {
                QuestionInfo info = new QuestionInfo();
                info.setId(q.getId());
                info.setTitle(q.getTitle());
                info.setPublisher(userInfoMapper.selectUserInfoByUid(q.getPublisherId()).getUsername());
                info.setDescription(q.getDescription());
                info.setGmtCreate(q.getGmtCreate());
                info.setGmtModified(q.getGmtModified());
                info.setCommentCount(q.getCommentCount());
                info.setViewCount(q.getViewCount());
                info.setTag(q.getTag());
                info.setIsDing(q.getIsDing() == 1 ? "是" : "否");
                info.setIsJing(q.getIsJing() == 1 ? "是" : "否");

                res.add(info);
            }
        }
        return res;
    }

    /**
     * 查询带关键字查询的问题数量
     *
     * @param keyword
     * @return
     */
    public int getAdminQuestionWithKeywordTotalCount(String keyword) {
        return questionMapper.getAllQuestionsWithKeyword(keyword).size();
    }


    /**
     * 查询所有问题，并转换成前端VO
     *
     * @return
     */
    public List<QuestionInfo> queryAllQuestionInfo() {
        List<Question> questions = questionMapper.getAllQuestions();
        List<QuestionInfo> res = new ArrayList<>();

        for (Question q : questions) {
            QuestionInfo info = new QuestionInfo();
            info.setId(q.getId());
            info.setTitle(q.getTitle());
            info.setPublisher(userInfoMapper.selectUserInfoByUid(q.getPublisherId()).getUsername());
            info.setDescription(q.getDescription());
            info.setGmtCreate(q.getGmtCreate());
            info.setGmtModified(q.getGmtModified());
            info.setCommentCount(q.getCommentCount());
            info.setViewCount(q.getViewCount());
            info.setTag(q.getTag());
            info.setIsDing(q.getIsDing() == 1 ? "是" : "否");
            info.setIsJing(q.getIsJing() == 1 ? "是" : "否");

            res.add(info);
        }
        return res;
    }

    /**
     * 获取问题的总数量
     *
     * @return 数量
     */
    public int getAdminQuestionTotalCount() {
        return questionMapper.getAllQuestions().size();
    }

    /**
     * 更新加精状态
     *
     * @param questionId 问题ID
     * @return 更新行数
     */
    public int updateJingForQuestionById(Integer questionId) {
        Question ques = questionMapper.selectByPrimaryKey(questionId);
        UserInfo userInfo = userInfoMapper.selectUserInfoByUid(ques.getPublisherId());

        if (ques.getIsJing() == 0) {
            //未加精，加精+10分
            UserRate userRate = userRateMapper.selectRateById(userInfo.getUId());
            userRateMapper.updateRateById(userInfo.getUId(), userRate.getRate() + 10);  //加精+10分

            return questionMapper.updateJingStatusByQuestionId(1, questionId);
        } else {
            //取消加精，-10分
            UserRate userRate = userRateMapper.selectRateById(userInfo.getUId());
            userRateMapper.updateRateById(userInfo.getUId(), userRate.getRate() - 10);  //取消加精-10分

            return questionMapper.updateJingStatusByQuestionId(0, questionId);
        }
    }

    /**
     * 更新帖子的置顶状态
     *
     * @param questionId 问题ID
     * @return 更新行数
     */
    public int updateDingForQuestionById(Integer questionId) {

        Question ques = questionMapper.selectByPrimaryKey(questionId);
        if (ques == null) return 0;

        UserInfo userInfo = userInfoMapper.selectUserInfoByUid(ques.getPublisherId());

        if (ques.getIsDing() == 0) {
            //置顶+15分
            UserRate userRate = userRateMapper.selectRateById(userInfo.getUId());
            userRateMapper.updateRateById(userInfo.getUId(), userRate.getRate() + 15);  //置顶+15分

            return questionMapper.updateDingStatusByQuestionId(1, questionId);
        } else {
            //取消置顶不扣分，毕竟不可能一直置顶
            return questionMapper.updateDingStatusByQuestionId(0, questionId);
        }
    }

    /**
     * 查询所有举报问题信息
     *
     * @return 举报信息VO
     */
    public List<QuestionReportVO> queryAllQuestionReport() {
        List<QuestionReport> temp = new ArrayList<>();
        List<QuestionReportVO> res = new ArrayList<>();

        temp.addAll(questionReportMapper.queryAllNotProcessReport());
        temp.addAll(questionReportMapper.queryAllProcessReport());

        for (QuestionReport q : temp) {
            res.add(convertQuestionReportToVO(q));
        }
        return res;
    }

    /**
     * 查询举报问题信息的数量
     *
     * @return 数量
     */
    public int queryAllQuestionReportCount() {
        List<QuestionReport> res = new ArrayList<>();

        res.addAll(questionReportMapper.queryAllNotProcessReport());
        res.addAll(questionReportMapper.queryAllProcessReport());

        return res.size();
    }

    /**
     * 查询带关键字的所有举报问题信息
     *
     * @return 举报问题信息VO
     */
    public List<QuestionReportVO> queryAllQuestionReportWithKeyword(String keyword) {
        List<QuestionReport> temp = new ArrayList<>();
        List<QuestionReportVO> res = new ArrayList<>();

        UserInfo userInfo = userInfoMapper.selectUserInfoByName(keyword);
        if (userInfo != null) {
            temp.addAll(questionReportMapper.queryAllReportWithKeyword(userInfo.getUId()));
        }

        for (QuestionReport q : temp) {
            res.add(convertQuestionReportToVO(q));
        }
        return res;
    }

    /**
     * 将举报信息转换成前端VO
     *
     * @param questionReport 举报实体类
     * @return  前端VO
     */
    private QuestionReportVO convertQuestionReportToVO(QuestionReport questionReport) {
        QuestionReportVO questionReportVO = new QuestionReportVO();

        questionReportVO.setId(questionReport.getId());
        questionReportVO.setUsername(userInfoMapper.selectUserInfoByUid(questionReport.getUserId()).getUsername());
        questionReportVO.setRUsername(userInfoMapper.selectUserInfoByUid(questionReport.getRUserId()).getUsername());
        questionReportVO.setRQuestion(questionMapper.selectByPrimaryKey(questionReport.getRQuestionId()).getTitle());
        questionReportVO.setReason(questionReport.getReportReason());
        questionReportVO.setIsProcess(questionReport.getIsProcess() == 0 ? "否" : "是");
        questionReportVO.setProcessResult(questionReport.getProcessResult());

        return questionReportVO;
    }

    /**
     * 更新处理状态
     *
     * @param flag
     * @param reportId
     * @return
     */
    public ForumResult updateReportResult(Integer flag, Integer reportId) {
        QuestionReport questionReport = questionReportMapper.queryReportById(reportId);

        if (questionReport != null && questionReport.getIsProcess() == 0) {
            //更新状态
            int i = questionReportMapper.updateQuestionReportStatusById(reportId, flag == 1 ? "同意该举报请求" : "该举报请求已驳回");

            return i > 0 ? ForumResult.ok() : new ForumResult(500, "处理该举报请求失败，请稍后再试", null);
        } else if (questionReport.getIsProcess() == 1) {
            return new ForumResult(500, "您已经处理过该问题的举报信息，请勿重复操作", null);
        } else {
            return new ForumResult(400, "暂无相关的举报请求信息", null);
        }
    }

    /**
     * 查询举报请求处理状态
     *
     * @param reportId
     * @return
     */
    public ForumResult getStatusForReport(Integer reportId) {
        QuestionReport questionReport = questionReportMapper.queryReportById(reportId);
        if (questionReport != null) {
            return questionReport.getIsProcess() == 0 ? ForumResult.ok() : new ForumResult(500, "该举报请求已经处理过", null);
        } else {
            return new ForumResult(400, "", null);
        }
    }
}
