package cn.itcast.travel.service;



/**
 * @author 张生祥
 * @date 2019/04/13 20:39
 */
public interface FavoriteService {
    boolean isFavorite(String rid, int uid);

    void addFavorite(String rid, int uid);

}
