package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.vo.UserInfoVO;
import cn.bestzuo.mjforum.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息Controller
 *
 * @author zuoxiang
 * @date 2019/11/24
 */
@Controller
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    /**
     * 跳转到用户信息页面
     *
     * 存在拦截请求，所以在这个页面上用户一定进行了登录
     *
     * @param request 请求
     * @param model   Model
     * @return 页面
     */
    @GetMapping("/settings")
    public String userInfo(HttpServletRequest request, Model model) {
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        UserInfo userInfo = userInfoService.selectUserInfoByUid(uid);

        //左侧侧边栏回显
        UserInfoVO infoVO = new UserInfoVO();
        infoVO.setAvatar(userInfo.getAvatar());
        infoVO.setUsername(userInfo.getUsername());
        infoVO.setComment((userInfo.getComment() == null || userInfo.getComment().length() == 0) ? "暂无个人评价" : userInfo.getComment());
        infoVO.setArea((userInfo.getArea() == null || userInfo.getArea().length() == 0) ? "暂无地址信息" : userInfo.getArea());
        infoVO.setCompany((userInfo.getCompany() == null || userInfo.getCompany().length() == 0) ? "暂无公司信息" : userInfo.getCompany());
        infoVO.setJobTitle((userInfo.getJobTitle() == null || userInfo.getJobTitle().length() == 0) ? "暂无职位信息" : userInfo.getJobTitle());

        //用户信息回显
        UserInfoVO vo = new UserInfoVO();
        vo.setUid(userInfo.getUId());
        vo.setAvatar(userInfo.getAvatar());
        vo.setLoginName(userInfo.getLoginName());
        vo.setUsername(userInfo.getUsername());
        vo.setCompany(userInfo.getCompany());
        vo.setJobTitle((userInfo.getJobTitle()));
        vo.setArea(userInfo.getArea());
        vo.setUniversity(userInfo.getUniversity());
        vo.setEmail(userInfo.getEmail());
        vo.setBirthday(userInfo.getBirthday());
        vo.setSex(userInfo.getSex());
        vo.setComment(userInfo.getComment());
        vo.setMajority(userInfo.getMajority());
        vo.setHobby(userInfo.getHobby());

        if (uid != null) {
            model.addAttribute("username", userInfo.getUsername());
            model.addAttribute("userinfo", vo);
            model.addAttribute("info", infoVO);
        }
        return "user/userinfo";
    }

    /**
     * 根据用户名获取用户头像地址
     *
     * @param uid 用户ID
     * @return 包装结果
     */
    @GetMapping("/getavatar")
    @ResponseBody
    public ForumResult getUserAvatar(@RequestParam("uid") Integer uid) {
        UserInfo userInfo = userInfoService.selectUserInfoByUid(uid);

        return userInfo == null ? new ForumResult(400, "", null) : new ForumResult(200, "", userInfo.getAvatar());
    }

    /**
     * 根据用户名查询用户信息，回显到页面上
     *
     * @param username 用户名
     * @return 包装结果
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
     * 根据用户ID查询用户信息
     *
     * @param uid 用户ID
     * @return 包装结果
     */
    @GetMapping("/getUserInfoByUid")
    @ResponseBody
    public ForumResult getUserInfoByUid(@RequestParam("uid") Integer uid) {
        UserInfo userInfo = userInfoService.selectUserInfoByUid(uid);
        if (userInfo == null) {
            return new ForumResult(500, "用户不存在", null);
        }
        return new ForumResult(200, "查询成功", userInfo);
    }


    /**
     * 根据用户名更新用户信息
     *
     * @return 包装结果
     */
    @PutMapping("/updateUserInfo")
    @ResponseBody
    public ForumResult updateUserInfo(UserInfo userInfo) {
        if (userInfo.getLoginName() == null || userInfo.getLoginName().length() == 0) {
            return new ForumResult(400, "用户非法操作", null);
        }

        if (userInfo.getUsername() == null || userInfo.getUsername().replaceAll(" ", "").length() == 0) {
            return new ForumResult(400, "用户昵称不能为空哦", null);
        }

        if (userInfo.getUsername().length() > 12) {
            return new ForumResult(400, "用户昵称不能超过12个字符哦", null);
        }

        UserInfo userInfoByName = userInfoService.getUserInfoByName(userInfo.getUsername());
        if (userInfoByName != null && !userInfoByName.getLoginName().equals(userInfo.getLoginName())) {
            return new ForumResult(400, "该用户昵称已经存在了哦", null);
        }

        try {
            userInfoService.updateUserInfo(userInfo);
            return new ForumResult(200, "更新信息成功", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "更新信息失败", null);
        }
    }

    /**
     * 根据用户UID更新用户信息
     *
     * @return 包装结果
     */
    @PutMapping("/updateUserInfoByUid")
    @ResponseBody
    public ForumResult updateUserInfoByUid(UserInfo userInfo) {
        System.out.println(userInfo.toString());
        try {
            userInfoService.updateUserInfoByUid(userInfo);
            return new ForumResult(200, "更新信息成功", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ForumResult(500, "更新信息失败", null);
        }
    }
}

