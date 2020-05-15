package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.mapper.QuestionMapper;
import cn.bestzuo.mjforum.mapper.QuestionReportMapper;
import cn.bestzuo.mjforum.mapper.UserInfoMapper;
import cn.bestzuo.mjforum.pojo.Question;
import cn.bestzuo.mjforum.pojo.QuestionReport;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.service.QuestionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 问题举报管理Service
 *
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/5 13:52
 */
@Service
public class QuestionReportServiceImpl implements QuestionReportService {

    @Autowired
    private QuestionReportMapper questionReportMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 处理问题举报
     * @param username  举报用户名
     * @param rUsername   被举报用户名
     * @param rQuestionId   被举报问题ID
     * @param reason  举报理由
     * @return  通用返回结果
     */
    @Override
    @Transactional
    public ForumResult processReport(String username, String rUsername, Integer rQuestionId, String reason) {
        //数据为空已校验
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(username);
        UserInfo rUserInfo = userInfoMapper.selectUserInfoByName(rUsername);
        Question question = questionMapper.selectByPrimaryKey(rQuestionId);
        if(userInfo == null || rUserInfo == null || question == null){
            return new ForumResult(400,"举报信息为空",null);
        }

        QuestionReport report = questionReportMapper.selectReportByUserIdAndQuestionId(userInfo.getUId(), question.getId());
        if(report != null){
            return new ForumResult(500,"您已经举报过该问题，请耐心等待我们的反馈!",null);
        }
        //插入数据库
        QuestionReport questionReport = new QuestionReport();
        questionReport.setUserId(userInfo.getUId());
        questionReport.setRUserId(rUserInfo.getUId());
        questionReport.setRQuestionId(question.getId());
        questionReport.setReportReason(reason);
        questionReport.setIsProcess(0);   //点击举报后，表示未处理
        questionReport.setProcessResult("暂未处理");

        int i = questionReportMapper.insertQuestionReport(questionReport);
        return i > 0 ? ForumResult.ok() : new ForumResult(500,"系统错误，请稍后再试",null);
    }
}
