package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.Collection;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 收藏问题Mapper
 */
@Component
public interface CollectionMapper {

    /**
     * 收藏问题
     * @param collection
     * @return
     */
    int insertCollection(Collection collection);

    /**
     * 更新收藏状态
     * @return
     */
    int updateCollectionStatus(Integer status,String username,String time,Integer questionId);

    /**
     * 查询收藏情况
     * @param username
     * @return
     */
    List<Collection> selectCollectionInfoByUsername(String username);

    /**
     * 查询指定的Collection信息
     * @param username
     * @param questionId
     * @return
     */
    Collection selectSpecificCollections(String username,Integer questionId);

    /**
     * 根据用户名和问题ID查找到对应问题收藏的状态
     * @param username
     * @param questionId
     * @return
     */
    Integer selectCollectionStatus(String username,Integer questionId);

    /**
     * 根据问题发布者查询该发布者的被收藏情况
     * @param publisher
     * @return
     */
    List<Collection> selectCollectionByPublisher(String publisher);
}
