package cn.bestzuo.mjforum.admin.controller;

import cn.bestzuo.mjforum.admin.common.LayuiTableResult;
import cn.bestzuo.mjforum.admin.listener.MyHttpSessionListener;
import cn.bestzuo.mjforum.admin.pojo.LoginInfo;
import cn.bestzuo.mjforum.admin.service.AdminLoginService;
import cn.bestzuo.mjforum.admin.utils.IpUtil;
import cn.bestzuo.mjforum.common.ForumResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 管理员登录请求
 */
@Controller
public class AdminIndexController {

    @Autowired
    private AdminLoginService loginService;

    /**
     * 跳转登录页面
     * @return
     */
    @GetMapping("/admin/login")
    public String index() {
        return "admin/login";
    }

    /**
     * 管理员页面首页
     * @return
     */
    @GetMapping("/admin")
    public String jumpToAdminIndex() {
        return "admin/index";
    }

    /**
     * 用户举报管理页面
     * @return
     */
    @GetMapping("/report")
    public String adminReport() {
        return "admin/user/report";
    }

    /**
     * 用户邮箱管理页面
     * @return
     */
    @GetMapping("/adminUserEmail")
    public String adminUserEmail() {
        return "admin/user/useremail";
    }

    /**
     * 用户管理页面
     * @return
     */
    @GetMapping("/adminUser")
    public String adminUser() {
        return "admin/user/usermanage";
    }

    /**
     * 问题管理页面
     * @return
     */
    @GetMapping("/adminQuestion")
    public String adminQuestion(){
        return "admin/question/questioninfo";
    }

    /**
     * 标签管理页面
     * @return
     */
    @GetMapping("/adminTag")
    public String adminTag(){
        return "admin/question/taginfo";
    }

    /**
     * 问题标签管理页面
     * @return
     */
    @GetMapping("/adminQuestionTag")
    public String adminQuestionTag(){
        return "admin/question/questiontag";
    }

    /**
     * 论坛概览
     * @return
     */
    @GetMapping("/summary")
    public String showSummary() {
        return "admin/summary";
    }

    /**
     * 编辑用户信息
     * @param uid  用户ID
     * @param model  model
     * @return
     */
    @GetMapping("/editUserInfo")
    public String editUse(@RequestParam("uid") String uid, Model model) {
        model.addAttribute("uid", uid);
        return "admin/user/edituserinfo";
    }

    /**
     * 登录请求
     *
     * @return  通用返回结果
     */
    @PostMapping("/admin/login")
    @ResponseBody
    public ForumResult login(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("verifyCode") String verifyCode,
                             HttpServletRequest request) throws Exception {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new ForumResult(400, "用户名或密码不能为空", null);
        }

        if(StringUtils.isEmpty(verifyCode))
            return new ForumResult(400,"验证码不能为空",null);

        //校验验证码
        //从session中取出verifyCode
        String rightCode = (String) request.getSession().getAttribute("verifyCode");
        if (!verifyCode.equalsIgnoreCase(rightCode)) {
            return new ForumResult(500, "验证码错误", null);
        }

        //校验用户名密码
        if (!username.equals("admin")) {
            return new ForumResult(400, "用户名或密码错误", null);
        }

        if (!password.equals("qwer123456...")) {
            return new ForumResult(400, "用户名或密码错误", null);
        }

        //获取登录IP
        String ip = IpUtil.getMyV4IP();

        /*
           获取登录IP地址信息
           此处调用接口时间过长，暂时取消
         */
        String info = "暂无详细地址信息";

        //获取时间
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        try {
            loginService.insertLoginInfo(ip, info, time);
            request.getSession().setAttribute("token","admin");
            return new ForumResult(200, "登录成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "查询失败", null);
        }
    }

    /**
     * 获取登录信息
     *
     * @return
     */
    @GetMapping("/getAdminLoginInfo")
    @ResponseBody
    public LayuiTableResult getLoginInfo() {
        try {
            List<LoginInfo> infos = loginService.queryAllLoginInfo();
            return new LayuiTableResult(0, "查询成功", infos.size(), infos);
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiTableResult(1, "查询失败", 0, null);
        }
    }

    /**
     * 获取论坛当前在线人数
     *
     * @return 通用结果
     */
    @GetMapping("/online")
    @ResponseBody
    public ForumResult getOnline() {
        int online = MyHttpSessionListener.online;
        return new ForumResult(200, "查询成功", online);
    }
}
