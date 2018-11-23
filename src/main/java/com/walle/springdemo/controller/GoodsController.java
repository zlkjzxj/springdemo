package com.walle.springdemo.controller;

import com.walle.springdemo.bean.Goods;
import com.walle.springdemo.bean.User;
import com.walle.springdemo.redis.GoodsKey;
import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.result.Result;
import com.walle.springdemo.service.GoodsService;
import com.walle.springdemo.service.UserService;
import com.walle.springdemo.vo.GoodsDetailVo;
import com.walle.springdemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 有时候为了兼容手机端token不会再cookie里，会从参数给传过来
     *
     * @param model
     * @param cookieToken
     * @param paramToken
     * @return
     *//*
    @RequestMapping("/tolist")
    public String toList(HttpServletResponse response,Model model,
                         @CookieValue(value = UserService.COOKIE_NAME, required = false) String cookieToken,
                         @RequestParam(value = UserService.COOKIE_NAME, required = false) String paramToken
    ) {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response,token);
        model.addAttribute("user", user);
        return "index";
    }*/

    /* */

    /**
     * 上面那种写法如果遇到多个功能都要传那么多参数，感觉很烦
     * 所以定义了一个argumentResolver来处理参数和逻辑（UserArgumentResolver）
     * <p>
     * <p>
     *
     * @param model
     * @return
     *//*
    @RequestMapping(value = "/to_list")
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        return "goods_list";
    }*/

    /*
     * <p>
     * <p>
     *缓存页面，因为之前的写法，用户每访问一次页面就要查询一次数据库，给数据库的压力很大，
     *扛不住，缓存下来就不用每次去查数据库了
     *produces ="text/html" @ResponseBody
     *
     *@param model
     *@return
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {

        //先判断有没有页面缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //如果是空，则手动来渲染
        model.addAttribute("user", user);
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine()
                .process("goods_list", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    /**
     * @param model
     * @return
     */
    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", user);
        //先判断有没有页面缓存
        String html = redisService.get(GoodsKey.getGetGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        //如果是空，则手动来渲染
        GoodsVo goodVo = goodsService.getGoodsVo(goodsId);
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (goodVo != null) {
            long startTime = goodVo.getStartDate().getTime();
            long endTime = goodVo.getEndDate().getTime();
            long now = System.currentTimeMillis();
            if (startTime > now) {//秒杀还没开始
                remainSeconds = (int) ((startTime - now) / 1000);
                miaoshaStatus = 0;
            } else if (now > endTime) {//秒杀已结束
                remainSeconds = -1;
                miaoshaStatus = 2;
            } else {//秒杀进行中
                miaoshaStatus = 1;
            }
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goodVo);

        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine()
                .process("goods_detail", context);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "" + goodsId, html);
        }
        return html;
    }

    /**
     * @param model
     * @return
     */
    @RequestMapping(value = "/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId) {
        GoodsVo goodVo = goodsService.getGoodsVo(goodsId);
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (goodVo != null) {
            long startTime = goodVo.getStartDate().getTime();
            long endTime = goodVo.getEndDate().getTime();
            long now = System.currentTimeMillis();
            if (startTime > now) {//秒杀还没开始
                remainSeconds = (int) ((startTime - now) / 1000);
                miaoshaStatus = 0;
            } else if (now > endTime) {//秒杀已结束
                remainSeconds = -1;
                miaoshaStatus = 2;
            } else {//秒杀进行中
                miaoshaStatus = 1;
            }
        }
        GoodsDetailVo detailVo = new GoodsDetailVo();
        detailVo.setUser(user);
        detailVo.setMiaoshaStatus(miaoshaStatus);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setGoodsVo(goodVo);

        return Result.success(detailVo);
    }
}
