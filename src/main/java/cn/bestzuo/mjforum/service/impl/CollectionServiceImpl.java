package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.mapper.CollectionMapper;
import cn.bestzuo.mjforum.mapper.UserInfoMapper;
import cn.bestzuo.mjforum.pojo.Collection;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 问题收藏Service
 */
@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    @Transactional
    public int insertCollection(Collection collection) {
        return collectionMapper.insertCollection(collection);
    }

    @Override
    @Transactional
    public int updateCollectionStatus(Integer status, String username, String time, Integer questionId) {
        return collectionMapper.updateCollectionStatus(status, username, time, questionId);
    }

    @Override
    public List<Collection> selectCollectionInfoByUsername(String username) {
        return collectionMapper.selectCollectionInfoByUsername(username);
    }

    @Override
    public Integer selectCollectionStatus(String username, Integer questionId) {
        return collectionMapper.selectCollectionStatus(username, questionId);
    }

    @Override
    public Collection selectSpecificCollections(String username, Integer questionId) {
        return collectionMapper.selectSpecificCollections(username, questionId);
    }

    @Override
    public List<Collection> selectCollectionByPublisher(String publisher) {
        return collectionMapper.selectCollectionByPublisher(publisher);
    }
}
