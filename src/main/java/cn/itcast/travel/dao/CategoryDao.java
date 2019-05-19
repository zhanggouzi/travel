package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/11 11:23
 */
public interface CategoryDao {
    List<Category> findAll();
}
