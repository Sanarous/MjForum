package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.Collection;

import java.util.List;

public interface CollectionService {
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
     * 根据用户名和问题ID查找到对应问题收藏的状态
     * @param username
     * @param questionId
     * @return
     */
    Integer selectCollectionStatus(String username,Integer questionId);

    /**
     * 查询指定的Collection信息
     * @param username
     * @param questionId
     * @return
     */
    Collection selectSpecificCollections(String username,Integer questionId);

    /**
     * 根据问题发布者查询该发布者的被收藏情况
     * @param publisher
     * @return
     */
    List<Collection> selectCollectionByPublisher(String publisher);
}
