package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.CommentNoticeInfoMapper;
import cn.bestzuo.zuoforum.pojo.CommentNoticeInfo;
import cn.bestzuo.zuoforum.service.CommentNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentNoticeServiceImpl implements CommentNoticeService {

    @Autowired
    private CommentNoticeInfoMapper commentNoticeInfoMapper;

    @Override
    @Transactional
    public int insertCommentNoticeInfo(CommentNoticeInfo record) {
        return commentNoticeInfoMapper.insert(record);
    }

    /**
     * 根据被通知者用户名查询通知消息
     * @param noticeName
     * @return
     */
    @Override
    public List<CommentNoticeInfo> selectCommentNoticeByName(String noticeName) {
        return commentNoticeInfoMapper.selectCommentNoticeByName(noticeName);
    }
}
