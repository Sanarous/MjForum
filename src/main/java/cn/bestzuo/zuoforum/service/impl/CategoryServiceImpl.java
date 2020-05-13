package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.CategoryMapper;
import cn.bestzuo.zuoforum.pojo.Categories;
import cn.bestzuo.zuoforum.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类信息Service
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 查询带有默认标签的所有分类信息
     * @return
     */
    @Override
    public List<Categories> getAllCategories() {
        return categoryMapper.getAllCategories();
    }
}
