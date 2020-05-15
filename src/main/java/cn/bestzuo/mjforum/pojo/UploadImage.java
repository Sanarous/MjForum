package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 上传图片的pojo信息
 */
@Data
public class UploadImage {

    //图片路径
    private String src;

    //图片名称，可选
    private String title;
}
