package cn.bestzuo.zuoforum.controller;

import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 七牛云后端token的Controller
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/7 11:56
 */
@Controller
@RequestMapping("/api/open/qiNiu")
public class QiNiuController {
    // 访问密钥
    private static final String ACCESS_KEY = "wQu-9N1NBXhZqfmwwAEG9MZzIfizMjej3rwq083z";
    // 应用编码
    private static final String SECRET_KEY = "byjc-2899hcURMiw2-F5Cm3rM4aKZjXxU3rBkaXl";
    // 访问空间
    private static final String BUCKET_NAME = "forum-bestzuo";

    /**
     * 后端获取七牛云存储桶的token
     * @param request
     * @param response
     */
    @RequestMapping("/getUpToken")
    public void getUpToken(HttpServletRequest request, HttpServletResponse response) {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String token = auth.uploadToken(BUCKET_NAME);
        //组装令牌返回前台
        String Uptoken = "{\"uptoken\":\"" + token + "\"}";
        try {
            response.setContentType("application/json;charset=utf-8");
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            PrintWriter out = response.getWriter();
            out.print(Uptoken);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
