package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.Question;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.SoundbankResource;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionMapperTest {

    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void test() {
//        List<Question> questions = questionMapper.getAllQuestions();
//        System.out.println(questions.get(0));
        Question question = questionMapper.selectByPrimaryKey(1);
        List<Question> questions = questionMapper.getAllQuestions();
        System.out.println(question);
        System.out.println(questions.get(0));
    }

    public static String getRandomCode(Integer code) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < code; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
