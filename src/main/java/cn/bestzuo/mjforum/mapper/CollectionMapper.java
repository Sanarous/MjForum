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
     * @param collection 收藏类
     * @return  更新行数
     */
    int insertCollection(Collection collection);

    /**
     * 更新收藏状态
     * @return 更新行数
     */
    int updateCollectionStatus(Integer status, String time, Integer uId, Integer questionId);

    /**
     * 查询收藏情况
     * @param uId  用户ID
     * @return  收藏信息
     */
    List<Collection> selectCollectionInfoByUid(Integer uId);

    /**
     * 查询指定的Collection信息
     * @param uId 用户ID
     * @param questionId  问题ID
     * @return  收藏信息
     */
    Collection selectSpecificCollections(Integer uId, Integer questionId);

    /**
     * 根据用户名和问题ID查找到对应问题收藏的状态
     * @param uId  用户ID
     * @param questionId  问题ID
     * @return 收藏状态
     */
    Integer selectCollectionStatus(Integer uId, Integer questionId);

    /**
     * 根据问题发布者查询该发布者的被收藏情况
     * @param publisherId 发布者ID
     * @return  收藏信息
     */
    List<Collection> selectCollectionByPublisher(Integer publisherId);
}
