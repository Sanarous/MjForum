package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 标签信息
 */
@Data
public class Tags {
    //标签ID
    private int id;

    //标签名
    private String tagsName;

    //对应的分类ID
    private int categoryId;

    //是否是默认标签
    private int isOriginTag;
}
