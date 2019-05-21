package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

/**
 * @author 张生祥
 * @date 2019/04/10 22:36
 */
public class UserServiceImpl implements UserService {
    UserDao dao = new UserDaoImpl();

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        User regisetUser = dao.findByUsername(user.getUsername());
        if (regisetUser != null) {
            //用户名已经存在，注册失败
            return false;
        }
        //用户名在数据库中不存在，则保存信息
        //1.设置用户激活码
        user.setCode(UuidUtil.getUuid());
        //2.设置激活状态为N，发送邮件再变为Y
        user.setStatus("N");
        //3.保存用户信息
        dao.save(user);
        //4.发送激活邮件
        //4.1 设置邮件内容,指定一个超链接到激活用户servlet
        // 并将激活码也携带到servlet,以便查询数据库判断是否点击了超链接
        String content = "<a href= 'http://localhost:8080/travel/user/activeUser?code=" + user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * 激活方法
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码来查询用户
        User byCode = dao.findByCode(code);
        //2.判断
        if (byCode != null){
            //如果用户不为空，调用dao的修改激活状态的方法，激活成功
            dao.updateStatus(byCode);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        return dao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }


}
