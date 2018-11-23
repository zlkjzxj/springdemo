package com.walle.springdemo.service;

import com.walle.springdemo.dao.GoodsDao;
import com.walle.springdemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVo(long id) {
        return goodsDao.getGoods(id);
    }

    public int reduceStock(long goodsId) {
        return goodsDao.reduceStock(goodsId);
    }
}
