package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author 张生祥
 * @date 2019/04/11 10:19
 */
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //声明UserService业务对象
    private UserService service = new UserServiceImpl();

    public void regist(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //验证码校验
        //1.获取参数
        String checkCode = request.getParameter("check");
        //获取session中的数据
        HttpSession session = request.getSession();
        String checkcodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        //清空session，防止验证码重复
        session.removeAttribute("CHECKCODE_SERVER");
        ResultInfo info = new ResultInfo();
        //判断
        if (checkcodeServer == null || !checkcodeServer.equalsIgnoreCase(checkCode)){
            //校验失败,将结果封装，通过json传给客户端
            info.setFlag(false);
            info.setErrorMsg("验证码有误");
            return;
        }
        //注册校验
        //获取请求参数
        Map<String, String[]> map = request.getParameterMap();
        //封装成对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service进行校验
        boolean registUser = service.regist(user);
        if (registUser){
            //如果结果为true则注册成功
            info.setFlag(true);
        }else {
            //注册失败，给出提示信息
            info.setFlag(false);
            info.setErrorMsg("注册失败，用户名已存在");
        }

        writeValue(info,response);
    }
    public void login(HttpServletRequest request, HttpServletResponse response) throws  IOException{
        //获取请求参数
        Map<String, String[]> map = request.getParameterMap();
        //封装为User对象
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service层来进行登录校验
        User loginUser = service.login(user);
        //创建结果集对象
        ResultInfo info = new ResultInfo();
        //判断用户对象是否为空
        if (loginUser == null){
            //如果为空，则提示信息
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        //判断是否激活
        if (loginUser != null && "N".equals(loginUser.getStatus())){
            //如果激活状态为N，提示信息
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请通过邮箱激活用户");
        }
        if (loginUser != null && "Y".equals(loginUser.getStatus())){
            //登录成功
            info.setFlag(true);
            //将用户对象存入session,以便登录成功后回显数据
            request.getSession().setAttribute("user",loginUser);
        }
        //响应数据给客户端
        //响应数据
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        //通过字节输出流的方式
        mapper.writeValue(response.getOutputStream(),info);
    }
    public void findUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端，用于登录后的回显
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");

        //通过字节输出流将user写回客户端
        mapper.writeValue(response.getOutputStream(),user);
    }
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //销毁session
        request.getSession().invalidate();
        //跳转到登录页面,使用重定向，防止刷新时，路径还未改变
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求参数，code
        String code = request.getParameter("code");
        if (code != null) {
            //调用service进行激活
            boolean flag = service.active(code);
            //判断标记
            String msg = null;
            if (flag) {
                //激活成功，设置提示登录信息
                msg = "激活成功，请<a href='login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败，请联系管理员!";
            }
            //响应到客户端,进行显示
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}
