package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author 张生祥
 * @date 2019/04/13 20:45
 */
public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite isFavorite(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where rid = ? and uid = ? ";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
            System.out.println(favorite);
        } catch (Exception e) {

        }
        return favorite;
    }

    /**
     * 根据id查询收藏次数
     *
     * @param rid
     * @return
     */
    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT count(*) FROM tab_favorite WHERE rid = ?";
        return template.queryForObject(sql, Integer.class, rid);
    }

    @Override
    public void addFavorite(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql, rid, new Date(), uid);
    }

    /**
     * 查询收藏总记录数
     *
     * @param uid
     * @return
     */
    @Override
    public int findFavoriteCount(int uid) {
        String sql = "select count(rid) from tab_favorite where uid = ?";
        int count = template.queryForObject(sql, Integer.class, uid);
        return count;
    }

    @Override
    public int findFavoriteRankCount(String rname, int p_start, int p_end) {
        //定义模板sql
        String sql = "select count(*) from tab_route where 1=1";
        //创建StringBuilder来存放查询条件
        StringBuilder sb = new StringBuilder(sql);
        //创建list集合来存储要传入的条件参数
        List params = new ArrayList();
        //参数判断
        if (rname != null && !"null".equals(rname) && rname.length() > 0){
            //如果cid不为0，再将条件加到sql中
            sb.append(" and  rname like ? ");
            params.add("%"+rname+"%");
        }
        if (p_start != 0 ){
            sb.append(" and  price > ? ");
            params.add(p_start);
        }
        if (p_end != 0 ){
            sb.append(" and  price < ? ");
            params.add(p_end);
        }
        /*System.out.println(sb.toString());
        System.out.println(params);*/
        return template.queryForObject(sb.toString(),Integer.class,params.toArray());
    }

}


