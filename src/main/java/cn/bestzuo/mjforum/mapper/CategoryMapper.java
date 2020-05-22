package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.Categories;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分类信息Mapper
 */
@Component
public interface CategoryMapper {

    /**
     * 查询所有分类信息
     * @return
     */
    @Select("select * from tb_categories")
    List<Categories> getAllCategories();
}
