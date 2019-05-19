package cn.itcast.travel.service;

import cn.itcast.travel.domain.Category;

import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/11 11:22
 */
public interface CategoryService {
    /**
     * 查询分类信息
     * @return
     */
    List<Category> findAll();
}
