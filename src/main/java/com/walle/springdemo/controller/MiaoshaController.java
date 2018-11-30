package com.walle.springdemo.controller;

import com.walle.springdemo.bean.MiaoshaOrder;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.rabbitmq.MiaoshaMsg;
import com.walle.springdemo.rabbitmq.MqSender;
import com.walle.springdemo.redis.GoodsKey;
import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.result.CodeMsg;
import com.walle.springdemo.result.Result;
import com.walle.springdemo.service.GoodsService;
import com.walle.springdemo.service.MiaoshaService;
import com.walle.springdemo.service.OrderService;
import com.walle.springdemo.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private MqSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<>();

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
            localOverMap.put(goods.getId(), false);
        }
    }

    @RequestMapping("/do_miaosha")
    public Result<Integer> doMiaosha(HttpServletResponse response, Model model, User user, @RequestParam("goodsId") long goodsId) {
        //判断用户是否登录，如果没有登录跳转到登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //利用内存标记来减少redis访问 假如商品有10个，过了10个之后就不在去执行下面的程序了
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MISAOSHA_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MISAOSHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.MISAOSHA_REPEAT);
        }
        MiaoshaMsg msg = new MiaoshaMsg();
        msg.setUser(user);
        msg.setGoodsId(goodsId);
        mqSender.sendMiaoshaMsg(msg);

        /**
         * 优化前的写法
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
        return Result.success(0);//排队中。。。
    }

    /**
     * 秒杀成功返回订单id
     * -1： 库存不足 返回
     * 0：还没处理完，排队中
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public Result<Integer> getMiaoshaResult(Model model, User user, @RequestParam("goodsId") long goodsId) {
        //判断用户是否登录，如果没有登录跳转到登录
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = miaoshaService.getResult(user.getId(), goodsId);
        return Result.success(0);
    }
}
