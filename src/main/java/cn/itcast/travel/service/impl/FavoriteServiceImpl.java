package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;

import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/13 20:42
 */
public class FavoriteServiceImpl implements FavoriteService {
    FavoriteDao dao = new FavoriteDaoImpl();
    /**
     * 根据数据库查询结果，来判断数据库中是否有收藏记录
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite favorite = dao.isFavorite(Integer.parseInt(rid), uid);
        return favorite!=null;
    }

    /**
     * 添加收藏方法，如果用户点击收藏进行添加
     * @param rid
     * @param uid
     */
    @Override
    public void addFavorite(String rid, int uid) {
        dao.addFavorite(Integer.parseInt(rid),uid);
    }
}
