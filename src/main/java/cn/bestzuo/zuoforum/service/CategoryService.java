package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.Categories;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有分类信息
     * @return
     */
    List<Categories> getAllCategories();
}
