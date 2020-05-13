package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.SearchMapper;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问题搜索Service
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchMapper searchMapper;

    /**
     * 根据标题和内容查询搜索结果
     * @param keywords  关键字，包含标题和内容
     * @return  问题信息
     */
    @Override
    public List<Question> searchByTitleAndContent(String keywords) {
        return searchMapper.searchByTitleAndContent(keywords);
    }
}
