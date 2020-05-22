package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.Tags;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 标签信息Mapper
 */
@Component
public interface TagMapper {

    /**
     * 查询数据库中的所有标签信息
     *
     * @return Tag信息
     */
    List<Tags> queryAllTags();

    /**
     * 根据标签名查询标签ID
     *
     * @param tagName 标签名
     * @return ID
     */
    Integer selectTagIdByTagName(String tagName);

    /**
     * 根据ID查询标签名
     *
     * @param tagId 标签ID
     * @return 名
     */
    String selectTagNameByTagId(Integer tagId);

    /**
     * 根据ID查询标签信息
     *
     * @param tagId 标签ID
     * @return 标签信息
     */
    Tags selectTagByTagId(Integer tagId);

    /**
     * 根据标签ID查询标签名，并且该标签为默认标签
     *
     * @param tagId 标签ID
     * @return 标签名
     */
    String selectTagNameByTagIdWithOrigin(Integer tagId);

    /**
     * 查询所有的默认tag信息
     *
     * @return tag实体类
     */

    List<Tags> queryAllTagsForOrigin();

    /**
     * 新增标签
     */
    int insertNewTag(Tags tag);

    /**
     * 新增问题和标签的对应关系
     *
     * @param questionId 问题ID
     * @param tagId      标签ID
     */
    void insertQuestionAndTag(@RequestParam("tagId") Integer tagId, @RequestParam("questionId") Integer questionId);


    /**
     * 根据问题ID查询对应的标签ID
     *
     * @param questionId
     * @return
     */
    List<Integer> selectTagIDsByQuestionId(Integer questionId);


    /**
     * 根据标签ID查询对应的问题ID
     *
     * @param tagId
     * @return
     */
    List<Integer> selectQuestionIdsByTagId(Integer tagId);
}
