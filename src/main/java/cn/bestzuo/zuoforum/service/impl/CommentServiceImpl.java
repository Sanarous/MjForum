package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.CommentMapper;
import cn.bestzuo.zuoforum.mapper.CommentNoticeInfoMapper;
import cn.bestzuo.zuoforum.mapper.QuestionMapper;
import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.pojo.Comment;
import cn.bestzuo.zuoforum.pojo.CommentNoticeInfo;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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


    /**
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


        //存在，查出头像地址
        if (userInfo.getAvatar().contains("https")) {
            com.setAvatar(userInfo.getAvatar());
        } else {
            String path = "https://forum-1258928558.cos.ap-guangzhou.myqcloud.com/";
            com.setAvatar(path + userInfo.getAvatar());
        }
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
