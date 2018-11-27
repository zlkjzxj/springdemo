package com.walle.springdemo.controller;

import com.walle.springdemo.bean.MiaoshaOrder;
import com.walle.springdemo.bean.OrderInfo;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.redis.GoodsKey;
import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.result.CodeMsg;
import com.walle.springdemo.service.GoodsService;
import com.walle.springdemo.service.MiaoshaService;
import com.walle.springdemo.service.OrderService;
import com.walle.springdemo.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    /**
     * 实现序列化后的方法
     * 用来初始化
     * 把秒杀的物品数量加载到redis中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.listGoodsVo();
        if (list == null) {
            return;
        }
        for (GoodsVo goods : list) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goods.getId(), goods.getStockCount());
        }
    }

    @RequestMapping("/do_miaosha")
    public String doMiaosha(HttpServletResponse response, Model model, User user, @RequestParam("goodsId") long goodsId) {
        //判断用户是否登录，如果没有登录跳转到登录
        if (user == null) {
            return "login";
        }
//        long stock = redisService.(GoodsKey.getMiaoshaGoodsStock,""+goodsId)
        /**
         * 优化之前的写法
         */
        /*//判断库存
        GoodsVo goodsVo = goodsService.getGoodsVo(goodsId);
        if (goodsVo.getStockCount() <= 0) {
            model.addAttribute("errMsg", CodeMsg.MISAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errMsg", CodeMsg.MISAOSHA_REPEAT.getMsg());
            return "miaosha_fail";
        }
        //减库存，下订单，一并完成
        OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";*/
        return "";
    }

}
