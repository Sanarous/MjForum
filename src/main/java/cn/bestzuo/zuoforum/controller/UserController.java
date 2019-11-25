package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.pojo.User;
import cn.bestzuo.zuoforum.service.UserService;
import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.util.JsonUtils;
import cn.bestzuo.zuoforum.util.MD5Password;
import cn.bestzuo.zuoforum.util.VerifyCode;
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
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 获取验证码图片
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
     * 查询用户名是否已被注册
     *
     * @param username
     * @return
     */
    @RequestMapping("/getUserByName")
    @ResponseBody
    public ForumResult getUserByName(@RequestParam("username") String username) {
        if (StringUtils.isEmpty(username)) {
            return new ForumResult(400, "用户名不能为空", null);
        }

        User user = userService.getUserByName(username);
        if (user == null)
            return new ForumResult(200, "用户名可用", null);
        else
            return new ForumResult(500, "用户名已被注册", null);
    }

    /**
     * 注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ForumResult register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("verifyCode") String verifyCode, HttpServletRequest request) {

        //后端数据校验
        if (StringUtils.isEmpty(username))
            return new ForumResult(400, "用户名不能为空", null);
        if (StringUtils.isEmpty(password))
            return new ForumResult(400, "密码不能为空", null);
        if (StringUtils.isEmpty(verifyCode))
            return new ForumResult(400, "验证码不能为空", null);

        String code = (String) request.getSession().getAttribute("verifyCode");
        //验证码校验
        if (code.compareToIgnoreCase(verifyCode) != 0)
            return new ForumResult(405, "验证码错误", null);

        //插入  用户信息表
        int res = userService.insertUser(username, password);
        if (res > 0)
            return new ForumResult(200, "注册成功", username);
        else
            return new ForumResult(500, "注册失败，请稍后再试", username);
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return json
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ForumResult loginUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        //后端校验用户名
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return new ForumResult(500, "用户名或密码不能为空", null);
        }

        User user = userService.getUserByName(username);
        if (user == null) {
            return new ForumResult(500, "用户不存在", null);
        }

        //校验密码
        if (!MD5Password.verify(password, user.getPassword())) {
            return new ForumResult(500, "密码错误", null);
        }

        //登录成功，跳转首页
        user.setPassword(null);  //清除密码

        //将用户信息存在session中
        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("uid",user.getUid());

        return new ForumResult(200, "登录成功", user);
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
        }
        return "redirect:/";
    }
}
