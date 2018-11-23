package com.walle.springdemo.dao;

import com.walle.springdemo.bean.MiaoshaOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

@Mapper
public interface MiaoshaOrderDao {
    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into miaosha_order (user_id,order_id,goods_id) values(#{userId},#{orderId},#{goodsId}) ")
    public int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
