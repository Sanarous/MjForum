package cn.bestzuo.mjforum.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.List;

/**
 * 自定义全局响应结果包装体
 * 用于包装通用返回结果，响应给页面json解析
 *
 * @author zuoxiang
 * @date 2019/11/15
 */
@Data
public class ForumResult {
    //定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    //响应业务状态
    private int status;

    //响应消息
    private String msg;

    //响应中的数据
    private Object data;

    public ForumResult(int status,String msg,Object data){
        this.data = data;
        this.msg = msg;
        this.status = status;
    }

    public ForumResult(Object data){
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public ForumResult(){

    }

    public static ForumResult build(int status, String msg, Object data) {
        return new ForumResult(status,msg,data);
    }

    /**
     * 用于有返回结果的成功响应
     * @param data
     * @return
     */
    public static ForumResult ok(Object data){
        return new ForumResult(200,"",data);
    }

    /**
     * 操作成功的返回
     * @return
     */
    public static ForumResult ok(){
        return new ForumResult(200,"",null);
    }

    public static ForumResult build(int status, String msg) {
        return new ForumResult(status,msg,null);
    }

    /**
     * 将json对象转换成ForumResult对象
     * @param jsonData  json数据
     * @param clazz   对象
     * @return 包装结果
     */
    public static ForumResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, ForumResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有Object对象的转换
     * @param json  json数据
     * @return 包装结果
     */
    public static ForumResult format(String json){
        try {
            return MAPPER.readValue(json, ForumResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json数据转换成集合对象
     * @param jsonData json数据
     * @param clazz  对象
     * @return 包装结果
     */
    public static ForumResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
}
