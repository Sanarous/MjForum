package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.CommentNoticeInfoMapper;
import cn.bestzuo.zuoforum.mapper.CommentReplyMapper;
import cn.bestzuo.zuoforum.mapper.QuestionMapper;
import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.pojo.CommentNoticeInfo;
import cn.bestzuo.zuoforum.pojo.CommentReply;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 楼中楼回复Service
 */
@Service
public class CommentReplyServiceImpl implements CommentReplyService {

    @Autowired
    private CommentNoticeInfoMapper commentNoticeInfoMapper;

    @Autowired
    private CommentReplyMapper commentReplyMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 新增一条回复
     * 1. 新增评论回复表
     * 2. 新增评论通知表
     */
    @Override
    @Transactional
    public CommentReply insertCommentReply(Integer qId, String username, String content, Integer parentCommentId, String replyfor) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(username);    //当前回复用户
        UserInfo userInfo1 = userInfoMapper.selectUserInfoByName(replyfor);    //目标回复用户

        //1. 插入评论回复表
        CommentReply commentReply = new CommentReply();
        commentReply.setRContent(content);
        commentReply.setRName(username);  //当前回复者
        commentReply.setRUid(userInfo.getUId());   //当前回复者的ID
        commentReply.setRAvatar(userInfo.getAvatar());
        commentReply.setTouname(replyfor); //目标回复的用户名
        commentReply.setTouid(userInfo1.getUId());  //目标回复的用户Id
        commentReply.setParentCommentId(parentCommentId); //父评论ID

        //时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        commentReply.setRTime(sdf.format(date));

        commentReplyMapper.insertCommentReply(commentReply);

        //2. 插入评论回复表，如果评论者和回复者ID相同，不进行通知
        if (!userInfo.getUId().equals(userInfo1.getUId())) {
            //进行通知
            CommentNoticeInfo commentNoticeInfo = new CommentNoticeInfo();
            commentNoticeInfo.setParentCommentId(parentCommentId);  //插入这条回复的父评论ID
            commentNoticeInfo.setCommentId(userInfo.getUId());
            commentNoticeInfo.setCommentName(username);
            commentNoticeInfo.setNoticeId(userInfo1.getUId());
            commentNoticeInfo.setNoticeName(userInfo1.getUsername());
            commentNoticeInfo.setContent(content);
            commentNoticeInfo.setQuestionId(qId);
            commentNoticeInfo.setTime(sdf.format(date));
            commentNoticeInfo.setStatus(0);

            //插入评论通知表
            commentNoticeInfoMapper.insert(commentNoticeInfo);
        }

        return commentReply;
    }

    /**
     * 根据评论ID查找楼中楼回复
     *
     * @param commentId
     * @return
     */
    @Override
    public List<CommentReply> queryReplyByCommentId(Integer commentId) {
        return commentReplyMapper.queryReplyByCommentId(commentId);
    }

    /**
     * 查询一个评论下所有回复数
     *
     * @param parentCommentId
     * @return
     */
    @Override
    public int queryReplyNum(Integer parentCommentId) {
        return commentReplyMapper.queryReplyNum(parentCommentId);
    }
}
