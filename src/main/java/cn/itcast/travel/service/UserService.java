package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

/**
 * @author 张生祥
 * @date 2019/04/10 22:36
 */
public interface UserService {
    /**
     * 注册方法，校验用户名是否存在
     * @param user
     * @return 返回是否注册成功
     */
    boolean regist(User user);

    /**
     * 激活方法，用来激活用户获取邮箱地址
     * @param code
     * @return
     */
    boolean active(String code);

    /**
     * 登录验证，通过用户名和密码查询数据库即可
     * @param user
     * @return
     */
    User login(User user);
}
