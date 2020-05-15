package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.User;
import cn.bestzuo.mjforum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

/**
 * 安全设置Controller
 *
 * @author zuoxiang
 * @date 2019/11/16
 */
@Controller
public class SafeController {

    private final UserService userService;

    @Autowired
    public SafeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/safe")
    public String index(){
        return "user/safe";
    }


    /**
     * 修改密码操作
     * @param oldPass  旧密码
     * @param newPass  新密码
     * @param repeatNewPass  重复输入的新密码
     * @return
     */
    @RequestMapping(value = "/safe",method = RequestMethod.POST)
    @ResponseBody
    public ForumResult updatePassword(@RequestParam("username") String username,
                                      @RequestParam("oldPass") String oldPass,
                                      @RequestParam("newPass") String newPass,
                                      @RequestParam("repeatNewPass") String repeatNewPass){

        //数据校验
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass) || StringUtils.isEmpty(repeatNewPass)){
            return new ForumResult(400,"输入不能为空",null);
        }

        //数据长度校验
        if(newPass.trim().length() < 6 || newPass.trim().length() > 12){
            return new ForumResult(400,"密码长度必须在6-12个字符之间，且不能有空格",null);
        }

        //用户名是否存在校验
        User user = userService.getUserByName(username);
        if(user == null){
            return new ForumResult(500,"用户不存在",null);
        }

        //密码是否相等校验
        if(oldPass.equalsIgnoreCase(newPass)){
            return new ForumResult(500,"新密码与原密码不能相同",null);
        }

        int res = userService.updatePassword(username,oldPass,newPass);
        if(res < 0){
            return new ForumResult(500,"原密码输入错误",null);
        }else
            return new ForumResult(200,"修改成功",null);
    }
}
