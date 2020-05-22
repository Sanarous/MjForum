package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.mapper.CommentNoticeInfoMapper;
import cn.bestzuo.mjforum.mapper.QuestionMapper;
import cn.bestzuo.mjforum.mapper.UserInfoMapper;
import cn.bestzuo.mjforum.pojo.CommentNoticeInfo;
import cn.bestzuo.mjforum.pojo.Question;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.pojo.vo.CommentNoticeVO;
import cn.bestzuo.mjforum.service.CommentNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论通知Service
 */
@Service
public class CommentNoticeServiceImpl implements CommentNoticeService {

    private final CommentNoticeInfoMapper commentNoticeInfoMapper;

    private final UserInfoMapper userInfoMapper;

    private final QuestionMapper questionMapper;

    @Autowired
    public CommentNoticeServiceImpl(CommentNoticeInfoMapper commentNoticeInfoMapper, UserInfoMapper userInfoMapper, QuestionMapper questionMapper) {
        this.commentNoticeInfoMapper = commentNoticeInfoMapper;
        this.userInfoMapper = userInfoMapper;
        this.questionMapper = questionMapper;
    }


    @Override
    @Transactional
    public int insertCommentNoticeInfo(CommentNoticeInfo record) {
        return commentNoticeInfoMapper.insert(record);
    }

    /**
     * 根据被通知者用户名查询通知消息
     * @param noticeName 通知用户名
     * @return 通知消息
     */
    @Override
    public List<CommentNoticeInfo> selectCommentNoticeByName(String noticeName) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(noticeName);
        return commentNoticeInfoMapper.selectCommentNoticeByUId(userInfo.getUId());
    }

    /**
     * 将评论信息转换成前端展示的VO
     *
     * @param commentNoticeInfo
     * @return
     */
    private CommentNoticeVO convertCommentNoticeToVO(CommentNoticeInfo commentNoticeInfo) {
        CommentNoticeVO commentNoticeVO = new CommentNoticeVO();
        commentNoticeVO.setId(commentNoticeInfo.getId());

        //获取回复者头像
        UserInfo info = userInfoMapper.selectUserInfoByUid(commentNoticeInfo.getCommentId());
        commentNoticeVO.setCommentAvatar(info.getAvatar());
        commentNoticeVO.setParentCommentId(commentNoticeInfo.getParentCommentId());
        commentNoticeVO.setUsername(info.getUsername());
        commentNoticeVO.setContent(commentNoticeInfo.getContent());
        commentNoticeVO.setStatus(commentNoticeInfo.getStatus());
        commentNoticeVO.setQuestionId(commentNoticeInfo.getQuestionId());
        commentNoticeVO.setTime(commentNoticeInfo.getTime());

        //获取文章标题
        Question question = questionMapper.selectByPrimaryKey(commentNoticeInfo.getQuestionId());
        if (question.getTitle().length() > 20) {
            commentNoticeVO.setTitle(question.getTitle().substring(0, 20) + "...");
        } else {
            commentNoticeVO.setTitle(question.getTitle());
        }
        return commentNoticeVO;
    }
}
