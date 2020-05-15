package cn.bestzuo.mjforum.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * Layui流式加载响应返回包装结果
 *
 * @author zuoxiang
 * @date 2020/5/8 19:42
 */
@Data
public class LayuiFlowResult {
    //定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //响应业务状态
    private int status;

    //响应消息
    private String msg;

    //响应中的数据
    private Object data;

    //返回的总页数
    private int pages;

    public LayuiFlowResult(int status,String msg,Object data,int pages){
        this.data = data;
        this.msg = msg;
        this.status = status;
        this.pages = pages;
    }

    public LayuiFlowResult(Object data){
        this.status = 200;
        this.msg = "OK";
        this.data = data;
        this.pages = 0;
    }

    public LayuiFlowResult(){

    }

    public static LayuiFlowResult build(int status,String msg,Object data,int pages){
        return new LayuiFlowResult(status,msg,data,pages);
    }

    /**
     * 用于有返回结果的成功响应
     * @param data
     * @return
     */
    public static LayuiFlowResult ok(Object data){
        return new LayuiFlowResult(200,"",data,0);
    }

    /**
     * 操作成功的返回
     * @return
     */
    public static LayuiFlowResult ok(){
        return new LayuiFlowResult(200,"",null,0);
    }

    public static LayuiFlowResult build(int status,String msg){
        return new LayuiFlowResult(status,msg,null,0);
    }
}
