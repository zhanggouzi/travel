package cn.itcast.travel.dao;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/11 19:51
 */
public interface RouteDao {
    /**
     * 查询总记录数
     * @return
     */
    int findCount(int cid,String rname);

    /**
     * 模糊条件分页查询
     * @param cid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    List<Route> findPageData(int cid,int start,int pageSize,String rname);

    /**
     *通过rid查询线路表中的所有信息
     * @return
     */
    Route findOne(int rid);



    List<Route> findFavoritePageData(int uid, int start, int pageSize);

    List<Route> findFavoriteRankPageData(String rname, int p_start, int p_end, int start, int pageSize);
}
