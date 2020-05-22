package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.mapper.CollectionMapper;
import cn.bestzuo.mjforum.mapper.QuestionMapper;
import cn.bestzuo.mjforum.mapper.UserInfoMapper;
import cn.bestzuo.mjforum.pojo.Collection;
import cn.bestzuo.mjforum.pojo.Question;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 问题收藏Service
 *
 * @author zuoxiang
 * @date 2019/11/22
 */
@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionMapper collectionMapper;

    private final UserInfoMapper userInfoMapper;

    private final QuestionMapper questionMapper;

    @Autowired
    public CollectionServiceImpl(CollectionMapper collectionMapper, UserInfoMapper userInfoMapper, QuestionMapper questionMapper) {
        this.collectionMapper = collectionMapper;
        this.userInfoMapper = userInfoMapper;
        this.questionMapper = questionMapper;
    }

    /**
     * 新增收藏信息
     *
     * @param collection 收藏实体类
     * @return 更新行数
     */
    @Override
    @Transactional
    public int insertCollection(Collection collection) {
        return collectionMapper.insertCollection(collection);
    }

    /**
     * 更新收藏状态
     *
     * @param status      状态 0-未收藏 1-已收藏
     * @param time        时间
     * @param questionId  问题ID
     * @param publisherId 问题发布者ID
     * @return 更新行数
     */
    @Override
    @Transactional
    public int updateCollectionStatus(Integer status, String time, Integer questionId, Integer publisherId) {
        return collectionMapper.updateCollectionStatus(status, time, questionId, publisherId);
    }

    /**
     * 查询用户的收藏信息
     *
     * @param uid 用户ID
     * @return 收藏信息
     */
    @Override
    public List<Collection> selectCollectionInfoByUid(Integer uid) {
        return collectionMapper.selectCollectionInfoByUid(uid);
    }

    /**
     * 查询问题的收藏状态
     *
     * @param uid        用户ID
     * @param questionId 问题ID
     * @return 状态
     */
    @Override
    public Integer selectCollectionStatus(Integer uid, Integer questionId) {
        return collectionMapper.selectCollectionStatus(uid, questionId);
    }

    /**
     * 查询特征的收藏信息
     *
     * @param uid        用户ID
     * @param questionId 问题ID
     * @return 收藏信息
     */
    @Override
    public Collection selectSpecificCollections(Integer uid, Integer questionId) {
        return collectionMapper.selectSpecificCollections(uid, questionId);
    }

    /**
     * 处理问题收藏
     *
     * @param username   用户名
     * @param questionId 问题ID
     * @return 更新行数
     */
    @Override
    @Transactional
    public int processCollect(String username, Integer questionId) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(username);
        Question question = questionMapper.selectByPrimaryKey(questionId);

        //前端用户非法操作
        if (question == null || userInfo == null) return -1;

        Collection collect = collectionMapper.selectSpecificCollections(userInfo.getUId(), questionId);

        if (collect == null) {
            //第一次点击，直接进行收藏
            Collection collection = new Collection();
            collection.setUId(userInfo.getUId());
            collection.setQuestionId(questionId);
            collection.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));  //收藏时间
            collection.setPublisherId(question.getPublisherId());   //收藏的帖子发布者ID
            collection.setStatus(1);  // 1-已收藏 0-未收藏  第一次直接是已收藏状态

            //插入数据库,新增收藏信息
            collectionMapper.insertCollection(collection);

            //在Question数据库中更新收藏数
            int likeCount = question.getLikeCount();
            questionMapper.updateLikeCountById(likeCount + 1, questionId);

            //返回点击收藏后的收藏状态
            return 1;
        } else {
            //找到对应的问题ID
            if (collect.getQuestionId().equals(questionId)) {
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                //有这个问题的ID
                if (collect.getStatus() == 0) {   //未收藏
                    //修改收藏时间
                    collectionMapper.updateCollectionStatus(1, time, userInfo.getUId(), questionId);

                    //修改问题收藏数量
                    int likeCount = question.getLikeCount();
                    questionMapper.updateLikeCountById(likeCount + 1, questionId);

                    return 1;
                } else {   //已收藏
                    collectionMapper.updateCollectionStatus(0, time, userInfo.getUId(), questionId);

                    int likeCount = question.getLikeCount();
                    questionMapper.updateLikeCountById(likeCount - 1, questionId);

                    return 0;
                }
            }
        }
        return -1;
    }

    /**
     * 根据发帖用户查询收藏信息
     *
     * @param publisherId 发帖用户ID
     * @return 收藏信息
     */
    @Override
    public List<Collection> selectCollectionByPublisher(Integer publisherId) {
        return collectionMapper.selectCollectionByPublisher(publisherId);
    }
}
