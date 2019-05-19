package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

/**
 * @author 张生祥
 * @date 2019/04/10 22:42
 */
public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 存储用户信息
     * @param user
     */
    void save(User user);

    /**
     * 通过激活码查询用户
     * @param code
     */
    User findByCode(String code);

    /**
     * 修改激活用户状态
     * @param user
     */
    void updateStatus(User user);

    User findByUsernameAndPassword(String username, String password);
}
