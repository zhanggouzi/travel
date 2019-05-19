package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

/**
 * @author 张生祥
 * @date 2019/04/11 19:05
 */
public interface RouteService {
    /**
     * 根据cid,当前页码，每页显示的条数，查询数据库
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return  返回一个pageBean对象
     */
    PageBean<Route> pageQuery(int cid,int currentPage,int pageSize,String rname);

    /**
     * 查询详情方法
     * @return
     */
    Route findDetail(String rid);

    /**
     * 查询收藏的线路数据
     * @param uid
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageBean<Route> findFavorite(int uid, int currentPage, int pageSize);

    /**
     * 查询收藏线路排行榜数据
     * @param currentPage
     * @param pageSize
     * @param rname
     * @param p_start
     * @param p_end
     * @return
     */
    PageBean<Route> favoriteRank(int currentPage, int pageSize, String rname, int p_start, int p_end);
}
