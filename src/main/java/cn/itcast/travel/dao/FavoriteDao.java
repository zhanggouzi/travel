package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/13 20:42
 */
public interface FavoriteDao {
    /**
     * 根据两个id来查询数据库,返回收藏对象
     * @param rid
     * @param uid
     * @return
     */
    Favorite isFavorite(int rid, int uid);

    /**
     * 根据rid线路id,查询收藏次数
     * @param rid
     * @return
     */
    int findCountByRid(int rid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    void addFavorite(int rid, int uid);

    int findFavoriteCount(int uid);

    int findFavoriteRankCount(String rname, int p_start, int p_end);
}
