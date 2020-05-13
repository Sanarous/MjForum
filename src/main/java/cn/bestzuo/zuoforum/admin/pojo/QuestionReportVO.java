package cn.bestzuo.zuoforum.admin.pojo;

import lombok.Data;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/5 14:49
 */
@Data
public class QuestionReportVO {
    //自增主键
    private int id;

    //举报用户名
    private String username;

    //被举报用户名
    private String rUsername;

    //被举报问题标题
    private String rQuestion;

    //举报理由
    private String reason;

    //是否已处理
    private String isProcess;

    //处理结果
    private String processResult;
}
