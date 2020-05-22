package cn.bestzuo.mjforum.controller;

import cn.bestzuo.mjforum.common.EasyWebImageUploadResult;
import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.common.LayEditUploadImageResult;
import cn.bestzuo.mjforum.common.WangEditorResult;
import cn.bestzuo.mjforum.pojo.UploadImage;
import cn.bestzuo.mjforum.pojo.vo.UserVO;
import cn.bestzuo.mjforum.service.UserInfoService;
import cn.bestzuo.mjforum.util.TencentCOS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 图片上传Controller
 *
 * @author zuoxiang
 * @date 2019/11/19
 */
@Controller
public class ImageUploadController {

    @Value("${tencent.path}")
    private String IMAGE_PATH;

    private final UserInfoService userInfoService;

    @Autowired
    public ImageUploadController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/newUpload")
    @ResponseBody
    public ForumResult newUpload(@RequestParam("file") MultipartFile multipartFile,
                                 HttpServletRequest request,Model model) throws IOException {

        String username = (String)request.getSession().getAttribute("username");

        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
        assert fileName != null;
        if (fileName.lastIndexOf(".") < 0)
            return new ForumResult(500, "上传图片格式不正确", null);

        //获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        //如果不是图片
        if (!prefix.equalsIgnoreCase(".jpg") && !prefix.equalsIgnoreCase(".jpeg") && !prefix.equalsIgnoreCase(".svg") && !prefix.equalsIgnoreCase(".gif") && !prefix.equalsIgnoreCase(".png")) {
            return new ForumResult(500, "上传图片格式不正确", null);
        }

        //使用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile("imagesFile-" + System.currentTimeMillis(), prefix);

        //将Multifile转换成File
        multipartFile.transferTo(excelFile);

        //调用腾讯云工具上传文件
        String imageName = TencentCOS.uploadfile(excelFile, "avatar");

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        //存入图片名称，用于网页显示
        model.addAttribute("imageName", IMAGE_PATH + imageName);

        //更新数据库
        userInfoService.updateUserAvatar(IMAGE_PATH + imageName, username);

        //返回成功信息
        return new ForumResult(200, "头像更换成功", IMAGE_PATH + imageName);
    }

    /**
     * 上传头像
     */
    @PostMapping("/upload")
    @ResponseBody
    public ForumResult upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("username") String username, Model model) throws Exception {
        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
        assert fileName != null;
        if (fileName.lastIndexOf(".") < 0)
            return new ForumResult(500, "上传图片格式不正确", null);

        //获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        //如果不是图片
        if (!prefix.equalsIgnoreCase(".jpg") && !prefix.equalsIgnoreCase(".jpeg") && !prefix.equalsIgnoreCase(".svg") && !prefix.equalsIgnoreCase(".gif") && !prefix.equalsIgnoreCase(".png")) {
            return new ForumResult(500, "上传图片格式不正确", null);
        }

        //使用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile("imagesFile-" + System.currentTimeMillis(), prefix);

        //将Multifile转换成File
        multipartFile.transferTo(excelFile);

        //调用腾讯云工具上传文件
        String imageName = TencentCOS.uploadfile(excelFile, "avatar");

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        //存入图片名称，用于网页显示
        model.addAttribute("imageName", IMAGE_PATH + imageName);

        //更新数据库
        userInfoService.updateUserAvatar(IMAGE_PATH + imageName, username);

        //返回成功信息
        return new ForumResult(200, "头像更换成功", IMAGE_PATH + imageName);
    }

    /**
     * 删除临时文件
     *
     * @param files 临时文件，可变参数
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 富文本编辑器中上传图片信息
     *
     * @param multipartFile 文件
     * @return 包装结果
     */
    @PostMapping("/uploadImage")
    @ResponseBody
    public LayEditUploadImageResult uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
        assert fileName != null;
        if (fileName.lastIndexOf(".") < 0)
            return new LayEditUploadImageResult(1, "上传图片格式不正确", null);

        //获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        //如果不是图片
        if (!prefix.equalsIgnoreCase(".jpg") && !prefix.equalsIgnoreCase(".jpeg") && !prefix.equalsIgnoreCase(".svg") && !prefix.equalsIgnoreCase(".gif") && !prefix.equalsIgnoreCase(".png")) {
            return new LayEditUploadImageResult(1, "图片格式不正确", null);
        }

        //使用uuid作为文件名，防止生成的临时文件重复
        String iName = "imagesFile-" + System.currentTimeMillis();

        final File excelFile = File.createTempFile(iName, prefix);

        //将Multifile转换成File
        multipartFile.transferTo(excelFile);

        //调用腾讯云工具上传文件
        String imageName = TencentCOS.uploadfile(excelFile, "info");

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        String src = IMAGE_PATH + imageName;

        UploadImage uploadImage = new UploadImage();
        uploadImage.setSrc(src);
        uploadImage.setTitle(iName);

        return new LayEditUploadImageResult(0, "图片上传成功", uploadImage);

    }

    /**
     * 问题发布时的图片上传
     *
     * @param multipartFile  文件，限制为图片
     * @return 包装结果
     * @throws IOException 异常
     */
    @PostMapping("/uploadQuestionImages")
    @ResponseBody
    public EasyWebImageUploadResult publishQuestion(@RequestParam("ques") MultipartFile multipartFile) throws IOException {
        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //获取文件后缀
        assert fileName != null;
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        //使用uuid作为文件名，防止生成的临时文件重复
        String iName = "imagesFile-" + System.currentTimeMillis();

        final File excelFile = File.createTempFile(iName, prefix);

        //将Multifile转换成File
        multipartFile.transferTo(excelFile);

        String imageName;
        try {
            //调用腾讯云工具上传文件
            imageName = TencentCOS.uploadfile(excelFile, "info");

        } catch (Exception e) {
            e.printStackTrace();

            return new EasyWebImageUploadResult(null);
        }

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        //腾讯云返回的图片地址
        String src = IMAGE_PATH + imageName;

        //七牛云返回的图片url地址
        //String src = imageName;

        String[] res = {src};

        return new EasyWebImageUploadResult(src);
    }
}

