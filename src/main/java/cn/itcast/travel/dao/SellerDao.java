package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

/**
 * @author 张生祥
 * @date 2019/04/13 15:46
 */
public interface SellerDao {
    Seller findById(int sid);
}
