package cn.bestzuo.mjforum.common;

import lombok.Data;

/**
 * @author zuoxiang
 * @date 2020/5/16 17:35
 */
@Data
public class EasyWebImageUploadResult {

    private String location;

    public EasyWebImageUploadResult(){}

    public EasyWebImageUploadResult(String location){
        this.location = location;
    }
}
