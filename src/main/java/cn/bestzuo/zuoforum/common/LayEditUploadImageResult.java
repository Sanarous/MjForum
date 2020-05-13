package cn.bestzuo.zuoforum.common;

import lombok.Data;

/**
 * 富文本编辑器图片上传成功后返回的包装结果
 */
@Data
public class LayEditUploadImageResult {

    //返回code 0-成功 其它-失败
    private int code;

    //一般是上传失败后返回的信息
    private String msg;

    //携带的Object
    private Object data;

    public LayEditUploadImageResult(){

    }

    public LayEditUploadImageResult(int code,String msg,Object data){
        this.code = code;
        this.msg  = msg;
        this.data = data;
    }
}
