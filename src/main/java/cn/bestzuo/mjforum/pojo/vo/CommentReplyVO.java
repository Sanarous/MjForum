package cn.bestzuo.mjforum.pojo.vo;

import cn.bestzuo.mjforum.pojo.CommentReply;
import lombok.Data;

/**
 * @author zuoxiang
 * @date 2020/5/17 13:05
 */
@Data
public class CommentReplyVO extends CommentReply {

    //目标用户名
    private String touname;

    //用户头像
    private String rAvatar;

    //用户名
    private String rName;
}
