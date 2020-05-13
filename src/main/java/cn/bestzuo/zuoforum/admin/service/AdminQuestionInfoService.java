package cn.bestzuo.zuoforum.admin.service;

import cn.bestzuo.zuoforum.admin.pojo.QuestionInfo;
import cn.bestzuo.zuoforum.admin.pojo.QuestionReportVO;
import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.mapper.*;
import cn.bestzuo.zuoforum.pojo.*;
import cn.bestzuo.zuoforum.service.QuestionService;
import cn.bestzuo.zuoforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.PAForUserEnc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/3 20:02
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class AdminQuestionInfoService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionEditMapper questionEditMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private QuestionReportMapper questionReportMapper;

    @Autowired
    private UserRateMapper userRateMapper;

    /**
     * 查询所有问题，并转换成前端VO
     * @return
     */
    public List<QuestionInfo> queryAllQuestionInfoWithKeyword(String keyword){
        List<QuestionInfo> res = new ArrayList<>();
        List<Question> questions = questionMapper.getAllQuestionsWithKeyword(keyword);

        if(questions.size() != 0){
            for(Question q : questions){
                QuestionInfo info = new QuestionInfo();
                info.setId(q.getId());
                info.setTitle(q.getTitle());
                info.setPublisher(q.getPublisher());
                info.setDescription(q.getDescription());
                info.setGmtCreate(q.getGmtCreate());
                info.setGmtModified(q.getGmtModified());
                info.setCommentCount(q.getCommentCount());
                info.setViewCount(q.getViewCount());
                info.setTag(q.getTag());
                QuestionEdit questionEdit = questionEditMapper.queryQuestionEditInfoByQuestionId(q.getId());
                if(questionEdit == null){
                    info.setIsDing("否");
                    info.setIsJing("否");
                }else{
                    info.setIsJing(questionEdit.getIsJing() == 0 ? "否" : "是");
                    info.setIsDing(questionEdit.getIsDing() == 0 ? "否" : "是");
                }
                res.add(info);
            }
        }
        return res;
    }

    /**
     * 查询带关键字查询的问题数量
     * @param keyword
     * @return
     */
    public int getAdminQuestionWithKeywordTotalCount(String keyword){
        return questionMapper.getAllQuestionsWithKeyword(keyword).size();
    }


    /**
     * 查询所有问题，并转换成前端VO
     * @return
     */
    public List<QuestionInfo> queryAllQuestionInfo(){
        List<Question> questions = questionMapper.getAllQuestions();
        List<QuestionInfo> res = new ArrayList<>();

        for(Question q : questions){
            QuestionInfo info = new QuestionInfo();
            info.setId(q.getId());
            info.setTitle(q.getTitle());
            info.setPublisher(q.getPublisher());
            info.setDescription(q.getDescription());
            info.setGmtCreate(q.getGmtCreate());
            info.setGmtModified(q.getGmtModified());
            info.setCommentCount(q.getCommentCount());
            info.setViewCount(q.getViewCount());
            info.setTag(q.getTag());
            QuestionEdit questionEdit = questionEditMapper.queryQuestionEditInfoByQuestionId(q.getId());
            if(questionEdit == null){
                info.setIsDing("否");
                info.setIsJing("否");
            }else{
                info.setIsJing(questionEdit.getIsJing() == 0 ? "否" : "是");
                info.setIsDing(questionEdit.getIsDing() == 0 ? "否" : "是");
            }
            res.add(info);
        }
        return res;
    }

    /**
     * 获取问题的总数量
     * @return
     */
    public int getAdminQuestionTotalCount(){
        return questionMapper.getAllQuestions().size();
    }

    /**
     * 更新加精状态
     * @param questionId
     * @return
     */
    public int updateJingForQuestionById(Integer questionId){
        //查询问题是否存在
        QuestionEdit questionEdit = questionEditMapper.queryQuestionEditInfoByQuestionId(questionId);
        if (questionEdit == null){
            //说明该问题没有被添加进对应的数据库
            Question question = questionMapper.selectByPrimaryKey(questionId);
            QuestionEdit qe = new QuestionEdit();
            qe.setQuestionId(questionId);
            qe.setUserId(userInfoMapper.selectUserInfoByName(question.getPublisher()).getUId());
            qe.setIsJing(1);
            qe.setIsDing(0);

            //顺便给用户积分+10分
            UserRate userRate = userRateMapper.selectRateById(qe.getUserId());
            userRateMapper.updateRateById(qe.getUserId(),userRate.getRate() + 10);  //加精+10分
            return questionEditMapper.insertNewQuestionEditInfo(qe);
        }else{
            //查询加精状态
            Integer status = questionEditMapper.queryIsJingByQuestionId(questionId);
            if(status == 1){
                //说明是要取消加精，扣10分
                UserRate userRate = userRateMapper.selectRateById(questionEdit.getUserId());
                userRateMapper.updateRateById(questionEdit.getUserId(),userRate.getRate() - 10);  //取消加精-10分
            }else{
                UserRate userRate = userRateMapper.selectRateById(questionEdit.getUserId());
                userRateMapper.updateRateById(questionEdit.getUserId(),userRate.getRate() + 10);  //取消加精-10分
            }
            return questionEditMapper.updateJingForQuestionById(status == 0 ? 1 : 0,questionId);
        }
    }

    /**
     * 更新置顶状态
     * @param questionId
     * @return
     */
    public int updateDingForQuestionById(Integer questionId){
        //查询问题是否存在
        QuestionEdit questionEdit = questionEditMapper.queryQuestionEditInfoByQuestionId(questionId);
        if (questionEdit == null){
            //说明该问题没有被添加进对应的数据库
            Question question = questionMapper.selectByPrimaryKey(questionId);
            QuestionEdit qe = new QuestionEdit();
            qe.setQuestionId(questionId);
            qe.setUserId(userInfoMapper.selectUserInfoByName(question.getPublisher()).getUId());
            qe.setIsJing(0);
            qe.setIsDing(1);

            //置顶+15分
            UserRate userRate = userRateMapper.selectRateById(qe.getUserId());
            userRateMapper.updateRateById(qe.getUserId(),userRate.getRate() + 15);  //置顶+15分

            return questionEditMapper.insertNewQuestionEditInfo(qe);
        }else{
            //查询加精状态
            Integer status = questionEditMapper.queryIsDingByQuestionId(questionId);
            if(status == 1){
                //说明是要取消置顶，扣10分
                UserRate userRate = userRateMapper.selectRateById(questionEdit.getUserId());
                userRateMapper.updateRateById(questionEdit.getUserId(),userRate.getRate() - 15);  //取消置顶-15分
            }else{
                UserRate userRate = userRateMapper.selectRateById(questionEdit.getUserId());
                userRateMapper.updateRateById(questionEdit.getUserId(),userRate.getRate() + 15);  //取消置顶-15分
            }

            return questionEditMapper.updateDingForQuestionById(status == 0 ? 1 : 0,questionId);
        }
    }

    /**
     * 查询所有举报问题信息
     * @return
     */
    public List<QuestionReportVO> queryAllQuestionReport(){
        List<QuestionReport> temp = new ArrayList<>();
        List<QuestionReportVO> res = new ArrayList<>();

        temp.addAll(questionReportMapper.queryAllNotProcessReport());
        temp.addAll(questionReportMapper.queryAllProcessReport());

        for(QuestionReport q : temp){
            res.add(convertQuestionReportToVO(q));
        }
        return res;
    }

    /**
     * 查询举报问题信息的数量
     * @return
     */
    public int queryAllQuestionReportCount(){
        List<QuestionReport> res = new ArrayList<>();

        res.addAll(questionReportMapper.queryAllNotProcessReport());
        res.addAll(questionReportMapper.queryAllProcessReport());

        return res.size();
    }

    /**
     * 查询带关键字的所有举报问题信息
     * @return
     */
    public List<QuestionReportVO> queryAllQuestionReportWithKeyword(String keyword){
        List<QuestionReport> temp = new ArrayList<>();
        List<QuestionReportVO> res = new ArrayList<>();

        UserInfo userInfo = userInfoMapper.selectUserInfoByName(keyword);
        if(userInfo != null){
            temp.addAll(questionReportMapper.queryAllReportWithKeyword(userInfo.getUId()));
        }

        for(QuestionReport q : temp){
            res.add(convertQuestionReportToVO(q));
        }
        return res;
    }

    /**
     * 将举报信息转换成前端VO
     * @param questionReport
     * @return
     */
    public QuestionReportVO convertQuestionReportToVO(QuestionReport questionReport){
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
     * @param flag
     * @param reportId
     * @return
     */
    public ForumResult updateReportResult(Integer flag,Integer reportId){
        QuestionReport questionReport = questionReportMapper.queryReportById(reportId);

        if(questionReport != null && questionReport.getIsProcess() == 0){
            //更新状态
            int i = questionReportMapper.updateQuestionReportStatusById(reportId, flag == 1 ? "同意该举报请求" : "该举报请求已驳回");

            return i > 0 ? ForumResult.ok() : new ForumResult(500,"处理该举报请求失败，请稍后再试",null);
        }else if(questionReport.getIsProcess() == 1){
            return new ForumResult(500,"您已经处理过该问题的举报信息，请勿重复操作",null);
        }else{
            return new ForumResult(400,"暂无相关的举报请求信息",null);
        }
    }

    /**
     * 查询举报请求处理状态
     * @param reportId
     * @return
     */
    public ForumResult getStatusForReport(Integer reportId){
        QuestionReport questionReport = questionReportMapper.queryReportById(reportId);
        if(questionReport != null){
            return questionReport.getIsProcess() == 0 ? ForumResult.ok() : new ForumResult(500,"该举报请求已经处理过",null);
        }else{
            return new ForumResult(400,"",null);
        }
    }
}
