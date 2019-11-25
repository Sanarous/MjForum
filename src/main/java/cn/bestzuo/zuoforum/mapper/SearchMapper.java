package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.Question;

import java.util.List;

public interface SearchMapper {

    /**
     * 根据问题标题和内容搜索关键字
     * @param keywords 关键字
     * @return
     */
    List<Question> searchByTitleAndContent(String keywords);
}
