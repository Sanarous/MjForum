package cn.bestzuo.zuoforum.admin.common;

import lombok.Data;

/**
 * 表格渲染
 */
@Data
public class LayuiTableResult {

    //0-成功 其它-失败
    private int code;

    //消息
    private String message;

    //总共
    private Integer count;

    //返回的数据格式
    private Object data;

    public LayuiTableResult() {

    }

    public LayuiTableResult(int code, String message, Integer count, Object data) {
        this.code = code;
        this.message = message;
        this.count = count;
        this.data = data;
    }
}
