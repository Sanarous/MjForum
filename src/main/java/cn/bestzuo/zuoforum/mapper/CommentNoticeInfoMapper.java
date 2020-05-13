package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.CommentNoticeInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentNoticeInfoMapper {
    List<CommentNoticeInfo> selectCommentNoticeByName(String noticeName);

    int deleteByPrimaryKey(Integer id);

    int insert(CommentNoticeInfo record);

    int insertSelective(CommentNoticeInfo record);

    CommentNoticeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentNoticeInfo record);

    int updateByPrimaryKeyWithBLOBs(CommentNoticeInfo record);

    int updateByPrimaryKey(CommentNoticeInfo record);
}