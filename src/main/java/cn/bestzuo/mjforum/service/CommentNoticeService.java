package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.CommentNoticeInfo;

import java.util.List;

public interface CommentNoticeService {

    /**
     * 新增一条评论通知信息
     * @return
     */
    int insertCommentNoticeInfo(CommentNoticeInfo record);

    /**
     * 根据被通知者用户名查询通知消息
     * @param noticeName
     * @return
     */
    List<CommentNoticeInfo> selectCommentNoticeByName(String noticeName);
}
