package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.Collection;

import java.util.List;

/**
 * 帖子收藏Service
 */
public interface CollectionService {
    /**
     * 收藏问题
     *
     * @param collection 收藏实体类
     * @return 更新行数
     */
    int insertCollection(Collection collection);

    /**
     * 更新收藏状态
     * @param status  状态 0-未收藏 1-已收藏
     * @param time  时间
     * @param questionId 问题ID
     * @param publisherId  发布问题的用户ID
     * @return  更新行数
     */
    int updateCollectionStatus(Integer status, String time, Integer questionId, Integer publisherId);

    /**
     * 查询用户的收藏信息
     *
     * @param uid 用户ID
     * @return 收藏信息
     */
    List<Collection> selectCollectionInfoByUid(Integer uid);

    /**
     * 根据用户ID和问题ID查找到对应问题收藏的状态
     *
     * @param uid 用户ID
     * @param questionId  问题ID
     * @return 收藏状态
     */
    Integer selectCollectionStatus(Integer uid, Integer questionId);

    /**
     * 查询指定的Collection信息
     *
     * @param uid 用户ID
     * @param questionId 问题ID
     * @return 收藏信息
     */
    Collection selectSpecificCollections(Integer uid, Integer questionId);

    /**
     * 处理收藏问题
     *
     * @param username   用户Id
     * @param questionId 问题ID
     * @return 更新行数
     */
    int processCollect(String username, Integer questionId);

    /**
     * 根据问题发布者查询该发布者的被收藏情况
     *
     * @param publisherId 发布者ID
     * @return  收藏信息
     */
    List<Collection> selectCollectionByPublisher(Integer publisherId);
}
