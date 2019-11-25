package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.common.LayEditUploadImageResult;
import cn.bestzuo.zuoforum.common.WangEditorResult;
import cn.bestzuo.zuoforum.pojo.UploadImage;
import cn.bestzuo.zuoforum.service.UserInfoService;
import cn.bestzuo.zuoforum.util.TencentCOS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片上传Controller
 */
@Controller
public class ImageUploadController {

    @Value("${tencent.path}")
    private String IMAGE_PATH;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 上传头像
     */
    @RequestMapping("/upload")
    @ResponseBody
    public ForumResult upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("username") String username, Model model) throws Exception {
        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
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
        model.addAttribute("imageName", imageName);

        //更新数据库
        userInfoService.updateUserAvatar(imageName, username);

        //返回成功信息
        return new ForumResult(200, "头像更换成功", imageName);
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
     * @param multipartFile
     * @return
     */
    @RequestMapping("/uploadImage")
    @ResponseBody
    public LayEditUploadImageResult uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        //获取文件的名称
        String fileName = multipartFile.getOriginalFilename();

        //判断有无后缀
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
     * 发布问题时的图片上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("/uploadQuestionImages")
    @ResponseBody
    public WangEditorResult publishQuestion(@RequestParam("ques") MultipartFile multipartFile) throws IOException {
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
            return new WangEditorResult(1, null);
        }

        //程序结束时，删除临时文件
        deleteFile(excelFile);

        String src = IMAGE_PATH + imageName;

        String[] res = {src};

        return new WangEditorResult(0, res);
    }
}

