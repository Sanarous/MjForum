package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.Question;

import java.util.List;

public interface SearchService {

    /**
     * 根据问题标题和内容搜索关键字
     * @param keywords
     * @return
     */
    List<Question> searchByTitleAndContent(String keywords);
}
