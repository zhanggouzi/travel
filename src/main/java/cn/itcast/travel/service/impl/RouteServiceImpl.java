package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.ImgDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.ImgDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/11 19:11
 */
public class RouteServiceImpl implements RouteService {
    RouteDao dao = new RouteDaoImpl();
    ImgDao imgDao = new ImgDaoImpl();
    SellerDao sellerDao = new SellerDaoImpl();
    FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        PageBean<Route> pageBean = new PageBean<Route>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示的条数
        pageBean.setPageSize(pageSize);
        //调用dao查询总记录数
        int count = dao.findCount(cid,rname);
        pageBean.setTotalCount(count);
        //计算开始页码
        int start = (currentPage - 1) * pageSize;

        //调用dao查询每页显示的数据,需要传入cid ,start,每页显示条数pageSize
        List<Route> routeList = dao.findPageData(cid,start,pageSize,rname);
        pageBean.setList(routeList);
        //计算总页码
        int totalpage = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;
        pageBean.setTotalPage(totalpage);
        return pageBean;
    }

    /**
     * 查询旅游详情页的所有信息，封装到route对象里面
     * @param rid
     * @return
     */
    @Override
    public Route findDetail(String rid) {

        //1.调用dao层的findAll方法查询route基本属性
        Route route = dao.findOne(Integer.parseInt(rid));

        //2.调用ImgDao层findByRid方法来查询图片信息
        List<RouteImg> routeImgList = imgDao.findByRid(route.getRid());
        route.setRouteImgList(routeImgList);

        //3.调用sellerDao层findById方法来查询商家信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        //4.查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        //将收藏次数封装到route对象中
        route.setCount(count);
        return route;
    }

    @Override
    public PageBean<Route> findFavorite(int uid, int currentPage, int pageSize) {
        PageBean<Route> pageBean = new PageBean<Route>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示的条数
        pageBean.setPageSize(pageSize);
        //调用dao查询总记录数
        int count = favoriteDao.findFavoriteCount(uid);
        pageBean.setTotalCount(count);
        //计算开始页码
        int start = (currentPage - 1) * pageSize;

        //调用dao查询每页显示的数据,需要传入cid ,start,每页显示条数pageSize
        List<Route> routeList = dao.findFavoritePageData(uid,start,pageSize);
        pageBean.setList(routeList);
        //计算总页码
        int totalpage = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;
        pageBean.setTotalPage(totalpage);
        return pageBean;
    }

    @Override
    public PageBean<Route> favoriteRank(int currentPage, int pageSize, String rname, int p_start, int p_end) {
        //创建一个空的pageBean对象
        PageBean<Route> pageBean = new PageBean<Route>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示的条数
        pageBean.setPageSize(pageSize);
        //调用dao查询总记录数
        int count = favoriteDao.findFavoriteRankCount(rname,p_start,p_end);
        pageBean.setTotalCount(count);
        //计算开始页码




        int start = (currentPage - 1) * pageSize;

        //调用dao查询每页显示的数据,需要传入cid ,start,每页显示条数pageSize
        List<Route> routeList = dao.findFavoriteRankPageData(rname,p_start,p_end,start,pageSize);
        pageBean.setList(routeList);
        //计算总页码
        int totalpage = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;
//        System.out.println(totalpage);
        pageBean.setTotalPage(totalpage);
        return pageBean;
    }
}
