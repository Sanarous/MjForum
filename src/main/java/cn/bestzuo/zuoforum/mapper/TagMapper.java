package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.Tags;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 标签信息Mapper
 */
public interface TagMapper {

    /**
     * 根据标签名查询标签ID
     * @param tagName
     * @return
     */
    Integer selectTagIdByTagName(String tagName);

    /**
     * 根据ID查询标签名
     * @param tagId
     * @return
     */
    String selectTagNameByTagId(Integer tagId);

    /**
     * 查询所有的tag信息
     *
     * @return tag实体类
     */

    List<Tags> queryAllTags();

    /**
     * 新增标签
     */
    int insertNewTag(Tags tag);

    /**
     * 新增问题和标签的对应关系
     *
     * @param questionId
     * @param tagId
     */
    void insertQuestionAndTag(@RequestParam("tagId") Integer tagId, @RequestParam("questionId") Integer questionId);


    /**
     * 根据问题ID查询对应的标签ID
     * @param questionId
     * @return
     */
    List<Integer> selectTagIDsByQuestionId(Integer questionId);

    /**
     * 根据标签ID查询对应的问题ID
     * @param tagId
     * @return
     */
    List<Integer> selectQuestionIdsByTagId(Integer tagId);
}
