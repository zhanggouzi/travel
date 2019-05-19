package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/11 19:52
 */
public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 这里参数要传入cid,否则查询出来的页码数是所有线路的总页码
     * @param cid
     * @return
     */
    @Override
    public int findCount(int cid,String rname) {
//        String sql = "select count(*) from tab_route where cid = ? and rname like ?";
        //定义模板sql
        String sql = "select count(*) from tab_route where 1=1";
        //创建StringBuilder来存放查询条件
        StringBuilder sb = new StringBuilder(sql);
        //创建list集合来存储要传入的条件参数
        List params = new ArrayList();
        //参数判断
        if (cid != 0){
            //如果cid不为0，再将条件加到sql中
            sb.append(" and  cid = ? ");
            params.add(cid);
        }
        if (rname != null&&!"null".equals(rname) ){
            sb.append(" and  rname like ? ");
            params.add("%"+rname+"%");
        }

        return template.queryForObject(sb.toString(),Integer.class,params.toArray());
    }

    @Override
    public List<Route> findPageData(int cid, int start, int pageSize,String rname) {
//        String sql = "select * from tab_route where cid = ? and rname like ? limit ?,?";

        //定义模板sql
        String sql = "select * from tab_route where 1=1";
        //创建StringBuilder来存放查询条件
        StringBuilder sb = new StringBuilder(sql);
        //创建list集合来存储要传入的条件参数
        List params = new ArrayList();
        //参数判断
        if (cid != 0){
            //如果cid不为0，再将条件加到sql中
            sb.append(" and  cid = ?");
            params.add(cid);
        }
        if (rname != null&&!"null".equals(rname)){
            //如果cid不为0，再将条件加到sql中
            sb.append(" and  rname like ? ");
            params.add("%"+rname+"%");
        }
        sb.append(" limit ?,? ");
        params.add(start);
        params.add(pageSize);

        List<Route> routeList = template.query(sb.toString(), new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
        return routeList;
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }



    /**
     * 查询收藏的数据
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findFavoritePageData(int uid, int start, int pageSize) {
        //使用多表查询，uid和rid是一对多的关系
        String sql = "SELECT tr.* FROM `tab_route` tr,tab_favorite tf WHERE tr.rid=tf.rid AND tf.uid = ? limit ?,?";
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid, start, pageSize);
        return routeList;
    }

    /**
     * 根据排行来查询收藏数据
     * @param rname
     * @param p_start
     * @param p_end
     * @param start
     * @param pageSize
     * @return 返回一个包含线路对象的集合
     */
    @Override
    public List<Route> findFavoriteRankPageData(String rname, int p_start, int p_end, int start, int pageSize) {
        //定义模板sql
        String sql = "SELECT * FROM tab_route  WHERE 1=1";
        //创建一个StringBuilder来拼接sql
        StringBuilder sb = new StringBuilder(sql);
        //创建一个装参数的集合
        List params = new ArrayList();
        if (rname!=null && !"null".equals(rname) && rname.length()>0){
            sb.append(" and rname like ?");
            params.add("%"+rname+"%");
        }
        if (p_start != 0){
            sb.append(" and  price  > ? ");
            params.add(p_start);
        }
        if (p_end != 0 ){
            sb.append(" and price < ? ");
            params.add(p_end);
        }
        sb.append(" order by count desc limit ?,? ");
        params.add(start);
        params.add(pageSize);
        List<Route> routeList = template.query(sb.toString(), new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
        return routeList;
    }
}
