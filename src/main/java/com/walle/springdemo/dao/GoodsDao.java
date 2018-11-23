package com.walle.springdemo.dao;

import com.walle.springdemo.bean.Goods;
import com.walle.springdemo.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("select mg.miaosha_price, mg.stock_count,mg.start_date,mg.end_date,g.* from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select mg.miaosha_price, mg.stock_count,mg.start_date,mg.end_date,g.* from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodId}")
    public GoodsVo getGoods(long id);

    @Update("update miaosha_goods set stock_count = stock_count-1 where goods_id= #{goodsId}")
    public int reduceStock(long goodsId);
}
