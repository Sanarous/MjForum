package cn.bestzuo.zuoforum.pojo;

import lombok.Data;

/**
 * 问题和标签对应关系实体类
 */
@Data
public class QuestionTag {

    private int id;

    private int tagId;

    private int questionId;
}
