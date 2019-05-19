package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author 张生祥
 * @date 2019/04/11 11:23
 */
public class CategoryServiceImpl implements CategoryService {
    CategoryDao dao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //使用缓存进行优化
        //1.获取jedis客户端
        Jedis jedis = null;
        try {
            jedis = JedisUtil.getJedis();
        } catch (Exception e) {
            System.out.println("redis没启动");
        }
        List<Category> cs = null;
        // 判断jedis是否为空
        if (Objects.isNull(jedis)) {
            // 去数据库查询
            cs = dao.findAll();
        } else {
//        //2.使用sorted进行排序查询缓存。zadd key score value //存储的是value值
//        Set<String> categorySet = jedis.zrange("category", 0, -1);
            //查询sorted中的分数(cid)和值(cname) tuple类型,有序
            Set<Tuple> categorySet = jedis.zrangeWithScores("category", 0, -1);
            //3.判断查询集合是否为空
            //定义一个list集合存储类别对象
            if (categorySet == null || categorySet.size() == 0) {
                //如果为空，查询数据库
//            System.out.println("查询数据库....");
                cs = dao.findAll();
                //再将数据存入redis
                for (int i = 0; i < cs.size(); i++) {
                    //key score(cid) value(cname)
                    jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());
                }
            } else {
                //不为空，则从redis中读取数据
//            System.out.println("查询缓存.....");
                //将categorySet转为list集合返回即可
                cs = new ArrayList<Category>();
                for (Tuple tuple : categorySet) {
                    Category category = new Category();
                    //通过tuple获取分数
                    category.setCid((int) tuple.getScore());
                    //通过tuple获取cid
                    category.setCname(tuple.getElement());
                    //将封装好的类别放入集合中
                    cs.add(category);
                }
            }
        }
        return cs;
    }
}
