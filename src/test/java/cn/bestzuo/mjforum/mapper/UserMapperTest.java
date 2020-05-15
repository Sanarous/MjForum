package cn.bestzuo.mjforum.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserMapperTest {

    @Autowired
    private CommentLikeMapper commentLikeMapper;


    @Test
    public void test() {
        int status = 1;
        Integer commentId = 31;
        String commentUsername = "Sanarous";
        Integer questionId = 46;

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        int i = commentLikeMapper.updateCommentLike(1, time, 2);
        if(i > 0){
            System.out.println("更新成功");
        }else{
            System.out.println("更新失败");
        }
    }


    @Test
    public void timeTest(){

    }
}
