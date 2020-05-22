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
 *
 * @author zuoxiang
 * @date 2019/11/24
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentNoticeInfoMapper commentNoticeInfoMapper;

    private final CommentMapper commentMapper;

    private final QuestionMapper questionMapper;

    private final UserInfoMapper userInfoMapper;

    private final UserRateMapper userRateMapper;

    @Autowired
    public CommentServiceImpl(CommentNoticeInfoMapper commentNoticeInfoMapper, CommentMapper commentMapper, QuestionMapper questionMapper, UserInfoMapper userInfoMapper, UserRateMapper userRateMapper) {
        this.commentNoticeInfoMapper = commentNoticeInfoMapper;
        this.commentMapper = commentMapper;
        this.questionMapper = questionMapper;
        this.userInfoMapper = userInfoMapper;
        this.userRateMapper = userRateMapper;
    }


    /**
     * 新增评论信息
     *      开启事务
     *      插入一条评论，需要注意：
     *          1. 插入评论表
     *          2. 插入评论通知表
     * @param username  用户名
     * @param comment  评论
     * @param questionId  问题ID
     * @return  评论实体类
     */
    @Override
    @Transactional
    public Comment insertCommentByQuestionId(String username, String comment, Integer questionId) {
        //1.插入评论表
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(username);
        Question question = questionMapper.selectByPrimaryKey(questionId);

        //后端校验
        if(userInfo == null || question == null){
            return null;
        }

        //封装Comment对象
        Comment com = new Comment();
        com.setComment(comment);
        com.setQuestionId(questionId);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        com.setTime(sdf.format(date));
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

        UserInfo noticeMan = userInfoMapper.selectUserInfoByUid(question.getPublisherId());
        commentNoticeInfo.setNoticeId(noticeMan.getUId());   //此时评论者就是文章的发布者
        commentNoticeInfo.setTime(sdf.format(date));
        commentNoticeInfo.setContent(comment);
        commentNoticeInfo.setQuestionId(questionId);
        commentNoticeInfo.setStatus(0);

        commentNoticeInfoMapper.insert(commentNoticeInfo);

        //评论其它用户发布的贴子，每评论一条用户积分+1，最多加3分
        //1.获取此时评论的用户信息
        UserRate userRate = userRateMapper.selectRateById(userInfo.getUId());

        //2.判断此时评论的用户和提问者用户是否相同，不相同才能加分
        if(!username.equalsIgnoreCase(noticeMan.getUsername())){
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
     * @param questionId 问题ID
     * @return 评论实体类
     */
    @Override
    public List<Comment> queryCommentByQuestionId(Integer questionId) {
        if (questionId == null) {
            return null;
        }
        return commentMapper.queryCommentByQuestionId(questionId);
    }

    /**
     * 根据用户名查询评论信息
     * @param uname  用户名
     * @return  评论信息
     */
    @Override
    public List<Comment> selectCommentsByUname(String uname) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(uname);
        return commentMapper.selectCommentsByUid(userInfo.getUId());
    }

    /**
     * 根据主键查询评论信息
     * @param id  主键
     * @return  评论信息
     */
    @Override
    public Comment selectCommentByPrimaryKey(Integer id) {
        return commentMapper.selectCommentByPrimaryKey(id);
    }
}
