package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.Question;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 搜索Mapper
 */
@Component
public interface SearchMapper {

    /**
     * 根据问题标题和内容搜索关键字
     *
     * @param keywords 关键字
     * @return 问题信息
     */
    List<Question> searchByTitleAndContent(String keywords);
}
