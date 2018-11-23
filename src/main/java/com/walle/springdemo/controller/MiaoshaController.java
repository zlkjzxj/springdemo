package com.walle.springdemo.controller;

import com.walle.springdemo.bean.MiaoshaOrder;
import com.walle.springdemo.bean.OrderInfo;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.result.CodeMsg;
import com.walle.springdemo.service.GoodsService;
import com.walle.springdemo.service.MiaoshaService;
import com.walle.springdemo.service.OrderService;
import com.walle.springdemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String doMiaosha(HttpServletResponse response, Model model, User user, @RequestParam("goodsId") long goodsId) {
        //判断用户是否登录，如果没有登录跳转到登录
        if (user == null) {
            return "login";
        }
        //判断库存
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
        return "order_detail";
    }
}
