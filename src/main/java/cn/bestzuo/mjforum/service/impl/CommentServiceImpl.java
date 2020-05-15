package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.mapper.*;
import cn.bestzuo.mjforum.pojo.*;
import cn.bestzuo.mjforum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 评论Service
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentNoticeInfoMapper commentNoticeInfoMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserRateMapper userRateMapper;


    /**
     * 新增评论信息
     * 开启事务
     * 插入一条评论，需要注意：
     * 1. 插入评论表
     * 2. 插入评论通知表
     *
     * @param comment
     * @return
     */
    @Override
    @Transactional
    public Comment insertCommentByQuestionId(String username, String comment, Integer questionId) {
        //1.插入评论表
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(username);
        Question question = questionMapper.selectByPrimaryKey(questionId);

        //封装Comment对象
        Comment com = new Comment();
        com.setComment(comment);
        com.setQuestionId(questionId);
        com.setUname(username);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        com.setTime(sdf.format(date));
        com.setAvatar(userInfo.getAvatar());
        com.setUid(userInfo.getUId());

        commentMapper.insertCommentByQuestionId(com);
        com.setCId(com.getCId());

        //2.然后数据库中评论数加1
        question.setCommentCount(question.getCommentCount() + 1);
        questionMapper.updateByPrimaryKeySelective(question);

        //3.插入评论通知表中
        CommentNoticeInfo commentNoticeInfo = new CommentNoticeInfo();
        commentNoticeInfo.setParentCommentId(com.getCId());  //插入新增的这条评论ID
        commentNoticeInfo.setCommentId(userInfo.getUId());
        commentNoticeInfo.setCommentName(username);

        UserInfo noticeMan = userInfoMapper.selectUserInfoByName(question.getPublisher());
        commentNoticeInfo.setNoticeId(noticeMan.getUId());   //此时评论者就是文章的发布者
        commentNoticeInfo.setNoticeName(question.getPublisher());
        commentNoticeInfo.setTime(sdf.format(date));
        commentNoticeInfo.setContent(comment);
        commentNoticeInfo.setQuestionId(questionId);
        commentNoticeInfo.setStatus(0);

        commentNoticeInfoMapper.insert(commentNoticeInfo);

        //评论其它用户发布的贴子，每评论一条用户积分+1，最多加3分
        //1.获取此时评论的用户信息
        UserRate userRate = userRateMapper.selectRateById(userInfo.getUId());

        //2.判断此时评论的用户和提问者用户是否相同，不相同才能加分
        if(!username.equalsIgnoreCase(question.getPublisher())){
            //3.判断该用户在同一帖子下回复数量没超过3次
            int count = commentMapper.selectOneUserCommentOnOneQuestionCount(userInfo.getUId(), questionId);
            if(count < 4){
                if(userRate == null){
                    UserRate rate = new UserRate();
                    rate.setUserId(userInfo.getUId());
                    rate.setRate(1);
                }else{
                    int rate = userRate.getRate();
                    userRateMapper.updateRateById(userInfo.getUId(),rate+1);
                }
            }
        }
        return com;
    }


    /**
     * 根据问题ID查询问题下所有评论信息
     *
     * @param questionId
     * @return
     */
    @Override
    public List<Comment> queryCommentByQuestionId(Integer questionId) {
        if (questionId == null) {
            return null;
        }
        return commentMapper.queryCommentByQuestionId(questionId);
    }

    @Override
    public List<Comment> selectCommentsByUname(String uname) {
        return commentMapper.selectCommentsByUname(uname);
    }

    @Override
    public Comment selectCommentByPrimaryKey(Integer id) {
        return commentMapper.selectCommentByPrimaryKey(id);
    }
}
