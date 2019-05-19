package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author 张生祥
 * @date 2019/04/11 11:15
 * 查询所有旅游线路类别，cid 和 线路名称
 */
@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    /**
     * 声明类别service对象
     */
    private  CategoryService service = new CategoryServiceImpl();
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用service查询数据
        List<Category> categoryList = service.findAll();
        writeValue(categoryList,response);
    }

}
