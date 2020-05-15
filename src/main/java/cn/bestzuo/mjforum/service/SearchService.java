package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.Question;

import java.util.List;

public interface SearchService {

    /**
     * 根据问题标题和内容搜索关键字
     * @param keywords
     * @return
     */
    List<Question> searchByTitleAndContent(String keywords);
}
