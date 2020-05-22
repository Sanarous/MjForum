package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.EmailInfo;
import cn.bestzuo.mjforum.pojo.User;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.vo.UserVO;
import cn.bestzuo.mjforum.service.EmailService;
import cn.bestzuo.mjforum.service.UserInfoService;
import cn.bestzuo.mjforum.service.UserService;
import cn.bestzuo.mjforum.util.MD5Password;
import cn.bestzuo.mjforum.util.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 用户登录注册Controller
 *
 * @author zuoxiang
 * @date 2019/11/16
 */
@Controller
public class UserLoginAndRegisterController {

    private final UserService userService;

    private final EmailService emailService;

    private final UserInfoService userInfoService;

    @Autowired
    public UserLoginAndRegisterController(UserService userService, EmailService emailService, UserInfoService userInfoService) {
        this.userService = userService;
        this.emailService = emailService;
        this.userInfoService = userInfoService;
    }

    /**
     * 跳转到注册页面
     *
     * @return 页面
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 跳转到登录页面
     *
     * @return 页面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 注册页面获取验证码图片
     *
     * @param response 响应
     * @param request  请求
     */
    @GetMapping("/getVerifyCode")
    public void getVerificationCode(HttpServletResponse response, HttpServletRequest request) {
        try {
            int width = 200;
            int height = 60;
            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //生成对应宽高的初始图片
            String randomText = VerifyCode.drawRandomText(width, height, verifyImg);
            //单独的一个类方法，出于代码复用考虑，进行了封装。
            //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
            request.getSession().setAttribute("verifyCode", randomText);
            response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
            OutputStream os = response.getOutputStream(); //获取文件输出流
            ImageIO.write(verifyImg, "png", os);//输出图片流
            os.flush();
            os.close();//关闭流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询该用户名是否已被注册
     *
     * @param username 用户名
     * @return 包装结果
     */
    @GetMapping("/getUserByName")
    @ResponseBody
    public ForumResult getUserByName(@RequestParam("username") String username) {
        //后端校验判空
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        return userService.getUserByName(username) == null ? ForumResult.ok() : new ForumResult(500, "用户名已被注册", null);
    }

    /**
     * 用户注册按钮
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ForumResult register(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("verifyCode") String verifyCode,
                                HttpServletRequest request) {

        //后端数据校验
        if (StringUtils.isEmpty(username))
            return new ForumResult(400, "用户名不能为空", null);
        if(username.length() < 2 || username.length() > 12)
            return new ForumResult(400,"用户名必须在2-12个字符之内",null);
        if (StringUtils.isEmpty(password))
            return new ForumResult(400, "密码不能为空", null);
        if (StringUtils.isEmpty(verifyCode))
            return new ForumResult(400, "验证码不能为空", null);

        //后端校验该用户名是否已经注册
        if(userService.getUserByName(username) != null)
            return new ForumResult(400,"该用户名已被注册",null);

        //后端校验用户名格式是否正确，要求：仅包含中文、英文字母、数字，且数字不能在最前面
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
        if(username.matches(regex) && !Character.isDigit(username.toCharArray()[0])){
            //放行，可以注册
            String code = (String) request.getSession().getAttribute("verifyCode");
            //验证码校验
            if (code.compareToIgnoreCase(verifyCode) != 0)
                return new ForumResult(405, "验证码错误", null);

            //插入用户信息表
            return userService.insertUser(username, password) > 0 ? ForumResult.ok() : new ForumResult(500, "注册失败，请稍后再试", username);
        }
        return new ForumResult(400,"用户名必须由中文、英文字母或者数字组成，且数字不能在最前面",null);
    }

    /**
     * 登录请求
     *
     * @param username 用户名
     * @param password 密码
     * @return json
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ForumResult loginUser(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("verifyCode") String verifyCode,
                                 HttpServletRequest request) {
        //后端校验用户名
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(verifyCode)) {
            return new ForumResult(500, "输入信息不能为空", null);
        }

        //判断用户名格式
        User user;
        String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        //先判断是否为邮箱格式，如果不为邮箱格式，那么就是使用用户名登录的
        if (username.matches(regex)) {
            //使用邮箱登录的，先验证邮箱是否验证过
            EmailInfo emailInfo = emailService.selectEmailInfoByEmail(username);

            //邮箱是否存在
            if (emailInfo == null) return new ForumResult(400, "邮箱不存在", null);

            //邮箱是否已经被激活
            if (emailInfo.getCheck() == 0) return new ForumResult(500, "邮箱未验证", null);

            //再根据邮箱查询密码
            user = userService.getUserByUserId(emailInfo.getUid());
        } else {
            user = userService.getUserByName(username);
            if (user == null) return new ForumResult(500, "用户不存在", null);
        }

        //后端校验密码
        if (!MD5Password.verify(password, user.getPassword())) {
            return new ForumResult(500, "密码错误", null);
        }
        //校验验证码
        String code = (String) request.getSession().getAttribute("verifyCode");
        if (code.compareToIgnoreCase(verifyCode) != 0)
            return new ForumResult(500, "验证码错误", null);

        //将用户信息存在session中
        UserInfo userInfo = userInfoService.selectUserInfoByUid(user.getUid());
        request.getSession().setAttribute("username", userInfo.getUsername());
        request.getSession().setAttribute("uid", userInfo.getUId());

        //将所有页面需要的用户信息存储到session中，主要就是ID和用户名
        UserVO userVO = new UserVO();
        userVO.setUid(userInfo.getUId());
        userVO.setUsername(userInfo.getUsername());

        //将所有需要用到的用户信息放到session中
        request.getSession().setAttribute("loginUserInfo",userVO);
        return ForumResult.ok();
    }

    /**
     * 注销
     *
     * @param username 用户名
     * @param request  请求
     * @return 页面
     */
    @GetMapping("/logout")
    public String logout(@RequestParam("username") String username, HttpServletRequest request) {
        String name = (String) request.getSession().getAttribute("username");
        if (name != null) {
            request.getSession().setAttribute("username", null);
            request.getSession().removeAttribute("username");
            request.getSession().invalidate();
        }
        return "redirect:/";
    }
}
