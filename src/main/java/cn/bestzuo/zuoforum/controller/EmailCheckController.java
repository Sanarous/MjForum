package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.service.EmailService;
import cn.bestzuo.zuoforum.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * 邮箱验证Controller
 */
@Controller
public class EmailCheckController {

    @Autowired
    private IMailService mailService;

    @Autowired
    private EmailService emailService;

    /**
     * 验证邮件
     *
     * @param emailTo
     * @return
     */
    @RequestMapping("/email")
    @ResponseBody
    public ForumResult emailCheck(@RequestParam("emailTo") String emailTo, HttpServletRequest request) {
        String randomCode = getRandomCode(6);
        try {
            mailService.sendSimpleMail(emailTo, "码匠论坛 邮箱验证", "您的邮箱验证码为：" + randomCode + "（15分钟后失效）");
            request.getSession().setAttribute("emailCode", randomCode);
            return new ForumResult(200, "邮件发送成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "邮件发送失败", null);
        }
    }

    /**
     * 随机生成6位数验证码
     *
     * @param code
     * @return
     */
    private String getRandomCode(Integer code) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < code; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }


    /**
     * 验证邮箱是否正确
     *
     * @param verifyCode 邮箱验证码
     * @return
     */
    @RequestMapping("/checkEmail")
    @ResponseBody
    public ForumResult checkEmail(@RequestParam("verifyCode") String verifyCode,
                                  @RequestParam("username") String username,
                                  @RequestParam("email") String email,
                                  HttpServletRequest request) {
        if (verifyCode == null || verifyCode.length() == 0) {
            return new ForumResult(400, "验证码不能为空", null);
        }

        String emailCode = (String) request.getSession().getAttribute("emailCode");
        try {
            if (emailCode.equalsIgnoreCase(verifyCode)) {
                //改变数据库状态
                //先查询状态
                Integer status = emailService.selectEmailCheckStatusByUsername(username);
                if (status == 0) {
                    //置1
                    emailService.updateEmailStatusByEmail(1, email);
                } else {
                    emailService.updateEmailStatusByEmail(0, email);
                }
                return new ForumResult(200, "验证成功", null);
            } else {
                return new ForumResult(500, "验证码不正确", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "验证失败", null);
        }
    }

    /**
     * 查询邮箱验证状态
     *
     * @param username 用户名
     * @return
     */
    @RequestMapping("/queryEmailStatus")
    @ResponseBody
    public ForumResult queryEmailStatus(@RequestParam("username") String username) {
        if (StringUtils.isEmpty(username))
            return new ForumResult(400, "用户名不能为空", null);

        try {
            Integer status = emailService.selectEmailCheckStatusByUsername(username);
            return new ForumResult(200, "查询成功", status);
        } catch (Exception e) {
            return new ForumResult(500, "查询失败", null);
        }
    }
}
