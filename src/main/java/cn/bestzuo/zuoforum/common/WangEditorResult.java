package cn.bestzuo.zuoforum.common;

import lombok.Data;

/**
 * WangEditor图片上传封装结果
 */
@Data
public class WangEditorResult {

    //错误代码  0-没有错误  其它-有错误
    private int errno;

    //若干图片地址
    private String[] data;

    public WangEditorResult() {
    }

    public WangEditorResult(int errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }
}
