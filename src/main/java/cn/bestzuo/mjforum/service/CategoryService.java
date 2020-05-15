package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.Categories;

import java.util.List;

public interface CategoryService {

    /**
     * 查询所有分类信息
     * @return
     */
    List<Categories> getAllCategories();
}
