package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.UserInfoService;
import cn.bestzuo.zuoforum.common.ForumResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息Controller
 */
@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    /**
     * 跳转到用户信息页面
     *
     * @param request 请求
     * @param model   Model
     * @return 页面
     */
    @RequestMapping("/user/settings")
    public String userInfo(HttpServletRequest request, Model model) {
        String username = (String) request.getSession().getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }
        return "user/userinfo";
    }

    /**
     * 根据用户名获取用户头像地址
     *
     * @param username 用户名
     * @return
     */
    @GetMapping("/getavatar")
    @ResponseBody
    public ForumResult getUserAvatar(String username) {
        UserInfo userInfo = userInfoService.getUserInfoByName(username);

        //绝对地址，先写死
        String result = "";
        String path = "https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/";
        if (userInfo == null) {
            return new ForumResult(500, "用户信息不存在", result);
        }
        //存在，查出头像地址
        String avatar = userInfo.getAvatar();
        if (avatar == null || avatar.trim().equals("")) {
            //为空就返回一个默认地址
            result = "https://bestzuo.cn/images/forum/anonymous.jpg";
            return new ForumResult(200, "默认头像", result);
        } else if (avatar.equalsIgnoreCase("https://bestzuo.cn/images/forum/anonymous.jpg")) {
            return new ForumResult(200, "默认头像", avatar);
        }

        //有头像的，直接查出来
        return new ForumResult(200, "查询成功", path + avatar);
    }

    /**
     * 根据用户名查询用户信息，回显到页面上
     *
     * @param username
     * @return
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    public ForumResult getUserInfo(@RequestParam("username") String username, Model model) {
        UserInfo userInfo = userInfoService.getUserInfoByName(username);
        if (userInfo == null) {
            return new ForumResult(500, "用户不存在", null);
        }
        return new ForumResult(200, "查询成功", userInfo);
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    @RequestMapping("/updateUserInfo")
    @ResponseBody
    public ForumResult updateUserInfo(UserInfo userInfo) {
        try {
            userInfoService.updateUserInfo(userInfo);
            return new ForumResult(200, "更新信息成功", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "更新信息失败", null);
        }
    }
}

