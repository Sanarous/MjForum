package cn.bestzuo.mjforum.admin.controller;

import cn.bestzuo.mjforum.admin.common.LayuiTableResult;
import cn.bestzuo.mjforum.admin.pojo.QuestionInfo;
import cn.bestzuo.mjforum.admin.pojo.QuestionReportVO;
import cn.bestzuo.mjforum.admin.service.AdminQuestionInfoService;
import cn.bestzuo.mjforum.common.ForumResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/3 19:47
 */
@Controller
public class AdminQuestionInfoController {

    private final AdminQuestionInfoService adminQuestionInfoService;

    @Autowired
    public AdminQuestionInfoController(AdminQuestionInfoService adminQuestionInfoService) {
        this.adminQuestionInfoService = adminQuestionInfoService;
    }

    /**
     * 查询问题信息
     * @param page  当前页面
     * @param limit  每页数量
     * @return  包装结果
     */
    @GetMapping("/getAdminQuestion")
    @ResponseBody
    public LayuiTableResult getAllQuestionInfo(@RequestParam("page") Integer page,
                                               @RequestParam("limit") Integer limit,
                                               @RequestParam(value = "key[username]", required = false) String keyword){
        if(keyword != null && keyword.length() != 0){
            //携带关键字进行查询
            PageHelper.startPage(page, limit);
            List<QuestionInfo> questionInfos = adminQuestionInfoService.queryAllQuestionInfoWithKeyword(keyword);
            PageInfo<QuestionInfo> pageInfo = new PageInfo<>(questionInfos);
            return new LayuiTableResult(0, "查询成功", adminQuestionInfoService.getAdminQuestionWithKeywordTotalCount(keyword), pageInfo.getList());
        }else{
            PageHelper.startPage(page, limit);
            List<QuestionInfo> questionInfos = adminQuestionInfoService.queryAllQuestionInfo();
            PageInfo<QuestionInfo> pageInfo = new PageInfo<>(questionInfos);

            return new LayuiTableResult(0, "查询成功", adminQuestionInfoService.getAdminQuestionTotalCount(), pageInfo.getList());
        }
    }

    /**
     * 更新加精状态
     * @param questionId 问题ID
     * @return 包装结果
     */
    @PutMapping("/updateJingForQuestionById")
    @ResponseBody
    public ForumResult updateJingForQuestionById(@RequestParam("questionId") Integer questionId){
        if(questionId == null) return new ForumResult(400,"问题ID不能为空",null);

        int i = adminQuestionInfoService.updateJingForQuestionById(questionId);

        return i > 0 ? ForumResult.ok() : new ForumResult(500,"添加精品问题失败，请稍后再试",null);
    }

    /**
     * 更新置顶状态
     * @param questionId 问题ID
     * @return  包装结果
     */
    @PutMapping("/updateDingForQuestionById")
    @ResponseBody
    public ForumResult updateDingForQuestionById(@RequestParam("questionId") Integer questionId){
        if(questionId == null) return new ForumResult(400,"问题ID不能为空",null);

        int i = adminQuestionInfoService.updateDingForQuestionById(questionId);

        return i > 0 ? ForumResult.ok() : new ForumResult(500,"添加置顶问题失败，请稍后再试",null);
    }

    /**
     * 查询所有举报信息
     * @param page
     * @param limit
     * @param keyword
     * @return
     */
    @GetMapping("/getReport")
    @ResponseBody
    public LayuiTableResult getAllReports(@RequestParam("page") Integer page,
                                          @RequestParam("limit") Integer limit,
                                          @RequestParam(value = "key[username]", required = false) String keyword) {
        if(keyword != null && keyword.length() != 0){
            //携带关键字进行查询
            PageHelper.startPage(page, limit);
            List<QuestionReportVO> questionReportVOS = adminQuestionInfoService.queryAllQuestionReportWithKeyword(keyword);
            PageInfo<QuestionReportVO> pageInfo = new PageInfo<>(questionReportVOS);
            return new LayuiTableResult(0, "查询成功", (int)pageInfo.getTotal(), pageInfo.getList());
        }else{
            PageHelper.startPage(page, limit);

            List<QuestionReportVO> questionReport = adminQuestionInfoService.queryAllQuestionReport();
            PageInfo<QuestionReportVO> pageInfo = new PageInfo<>(questionReport);

            return new LayuiTableResult(0, "查询成功", adminQuestionInfoService.queryAllQuestionReportCount(), pageInfo.getList());
        }
    }

    /**
     * 更新问题处理结果
     * @param flag  处理标志 0-未处理 1-已处理
     * @param reportId  举报ID
     * @return  包装结果
     */
    @PostMapping("/updateProcessResult")
    @ResponseBody
    public ForumResult updateReportResult(@RequestParam("processFlag") Integer flag,
                                          @RequestParam("reportId") Integer reportId){
        if(flag == null || reportId == null) return new ForumResult(400,"操作信息不能为空",null);

        return adminQuestionInfoService.updateReportResult(flag, reportId);
    }

    /**
     * 查询举报处理请求
     * @param reportId 举报ID
     * @return  包装结果
     */
    @GetMapping("/getStatusById")
    public ForumResult getStatusForReport(@RequestParam("reportId") Integer reportId){
        return adminQuestionInfoService.getStatusForReport(reportId);
    }
}
