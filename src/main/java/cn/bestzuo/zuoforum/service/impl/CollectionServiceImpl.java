package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.CollectionMapper;
import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.pojo.Collection;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.CollectionService;
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
