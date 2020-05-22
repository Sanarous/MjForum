package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.CommentNoticeInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论通知Mapper
 */
@Component
public interface CommentNoticeInfoMapper {
    List<CommentNoticeInfo> selectCommentNoticeByUId(Integer uid);

    int deleteByPrimaryKey(Integer id);

    int insert(CommentNoticeInfo record);

    int insertSelective(CommentNoticeInfo record);

    CommentNoticeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentNoticeInfo record);

    int updateByPrimaryKeyWithBLOBs(CommentNoticeInfo record);

    int updateByPrimaryKey(CommentNoticeInfo record);
}