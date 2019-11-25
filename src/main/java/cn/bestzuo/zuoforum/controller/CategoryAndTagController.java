package cn.bestzuo.zuoforum.controller;

import cn.bestzuo.zuoforum.pojo.Categories;
import cn.bestzuo.zuoforum.pojo.Tags;
import cn.bestzuo.zuoforum.service.CategoryService;
import cn.bestzuo.zuoforum.service.TagService;
import cn.bestzuo.zuoforum.common.ForumResult;
import cn.bestzuo.zuoforum.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 问题分类和标签Controller
 */
@Controller
public class CategoryAndTagController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    /**
     * 获取问题分类信息
     *
     * @return
     */
    @GetMapping("/getCategories")
    @ResponseBody
    public ForumResult getCategories() {
        List<Categories> categories = categoryService.getAllCategories();
        return new ForumResult(200, "查询成功", JsonUtils.objectToJson(categories));
    }

    /**
     * 获取标签信息
     *
     * @return
     */
    @GetMapping("/getTags")
    @ResponseBody
    public ForumResult getTags() {
        List<Tags> tags = tagService.queryAllTags();
        return new ForumResult(200, "查询成功", JsonUtils.objectToJson(tags));
    }
}
