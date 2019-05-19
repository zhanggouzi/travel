package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/13 15:40
 */
public interface ImgDao {
    /**
     * 通过id查询图片表返回图片对象的集合
     * @return
     */
    List<RouteImg> findByRid(int rid);
}
