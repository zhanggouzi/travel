package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/11 18:56
 * 分页数据查询，根据类别的id（国内游，出境游....）查询出每个线路的详细信息
 */
@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接收查询名称的请求参数rname
        String rname = request.getParameter("rname");
//        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        //处理参数
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }
        //调用service查询PageBean对象,要传入一个模糊查询的参数
        PageBean<Route> pageBean = service.pageQuery(cid, currentPage, pageSize, rname);
        writeValue(pageBean, response);
    }

    /**
     * 查询详情方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取参数
        String rid = request.getParameter("rid");
        //2.调用service查询数据库
        Route route = service.findDetail(rid);
        //3.封装为json对象返回给客户端
        writeValue(route, response);
    }

    /**
     * 判断用户收藏
     *
     * @param request
     * @param response
     * @return 返回是否收藏的布尔值
     * 根据cid和uid来查询favorite表，如果查询不到数据，则为没有收藏记录，如果有数据，则收藏成功
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取请求参数
        //1.1获取请求的rid
        String rid = request.getParameter("rid");
        //1.2通过session，获取uid
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user != null) {
            uid = user.getUid();
        } else {
            uid = 0;
        }
        //2.调用FavoriteService查询数据库

        boolean flag = favoriteService.isFavorite(rid, uid);
        writeValue(flag,response);
    }

    /**
     * 添加收藏的方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //1.获取请求参数
        //1.1获取请求的rid
        String rid = request.getParameter("rid");
        //1.2通过session，获取uid
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            //如果用户尚未登录，直接结束方法，防止将uid=0添加到数据库中
            return;
        } else {
            uid = user.getUid();
        }
        //2.调用FavoriteService查询数据库
        favoriteService.addFavorite(rid, uid);
    }

    /**
     * 查询用户收藏的线路信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取请求参数
        String uidStr = request.getParameter("uid");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        int uid = 0;
        if (uidStr != null && uidStr.length()>0 && !"null".equals(uidStr)){
            uid = Integer.parseInt(uidStr);
            if (uid == 0){
                return;
            }
        }
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 12;
        }
        //调用service查询数据库，返回对应的rid
        PageBean<Route> pageBean = service.findFavorite(uid, currentPage, pageSize);
        writeValue(pageBean,response);
    }

    /**
     * 查询所有收藏，进行排名展示
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void favoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        //获取请求参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        String p_startStr = request.getParameter("p_start");
        String p_endStr = request.getParameter("p_end");
        //处理参数，初始化
        //处理参数
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 8;
        }
        int p_start = 0;
        if (p_startStr != null && p_startStr.length() > 0 && !"null".equals(p_startStr)) {
            p_start = Integer.parseInt(p_startStr);
        } else {
            p_start = 0;
        }
        int p_end = 0;
        if (p_endStr != null && p_endStr.length() > 0 && !"null".equals(p_endStr)) {
            p_end = Integer.parseInt(p_endStr);
        } else {
            p_end = 0;
        }
        //调用service查询PageBean对象,要传入三个模糊查询的参数
        PageBean<Route> pageBean = service.favoriteRank(currentPage, pageSize, rname,p_start,p_end);
        writeValue(pageBean, response);
    }
}
