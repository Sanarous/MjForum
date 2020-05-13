package cn.bestzuo.zuoforum.util;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author zuoxiang
 * @version 1.0
 * @date 2020/5/7 15:37
 */
@Component
public class QiniuUtils {

    private static final Logger logger = LoggerFactory.getLogger(QiniuUtils.class);

//    @Value("${qiniu.accessKey}")
//    private String accessKey;
//
//    @Value("${qiniu.secretKey}")
//    private String secretKey;
//
//    @Value("${qiniu.bucket}")
//    private String bucket;
//
//    @Value("${qiniu.domain}")
//    private String path;

    private String accessKey = "wQu-9N1NBXhZqfmwwAEG9MZzIfizMjej3rwq083z";

    private String secretKey = "byjc-2899hcURMiw2-F5Cm3rM4aKZjXxU3rBkaXl";

    private String bucket = "forum-bestzuo";

    private String domain = "http://image.bestzuo.cn";

    /**
     * 将图片上传到七牛云
     *
     * @param file 文件
     * @return
     */
    public String uploadImg(File file, String fileName) {
        //构造一个带指定Zone对象的配置类,zone2表示华南
        Configuration cfg = new Configuration(Zone.zone2());

        //其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(file, fileName, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);

                //图片上传后的地址
                return domain + "/" + putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //忽略
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
