package cn.bestzuo.zuoforum.pojo;

import lombok.Data;

/**
 * 问题举报实体类
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/5 11:51
 */
@Data
public class QuestionReport {

    //自增ID
    private int id;

    //举报用户ID
    private int userId;

    //被举报用户ID
    private int rUserId;

    //被举报问题ID
    private int rQuestionId;

    //举报理由
    private String reportReason;

    //是否处理 0-否 1-是
    private int isProcess;

    //处理判决结果
    private String processResult;
}
