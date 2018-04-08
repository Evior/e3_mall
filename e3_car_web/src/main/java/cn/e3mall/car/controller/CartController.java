package cn.e3mall.car.controller;

import cn.e3mall.car.service.CartService;
import cn.e3mall.comment.utils.CookieUtils;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.comment.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Value("${COOKIE_CART_TIME}")
    private Integer COOKIE_CART_TIME;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    /**
     * 添加商品信息
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/add/{itemId}")
    private String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                           HttpServletRequest request, HttpServletResponse response){
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是否登录状态
        if (user!=null)
        {
            //已经登录
            //保存到服务端
            cartService.addCart(user.getId(), itemId, num);
            //返回逻辑视图
            return "cartSuccess";
        }
        //未登录
        //从cookie查询购物车数据
        List<TbItem> list = getCartListFormCookie(request);
        //判断购物车里是否有该商品
        Boolean flag=false;
        for (TbItem item:list)
        {
            if (item.getId()==itemId)
            {
                //找到商品 数量相加
                item.setNum(item.getNum()+num);
                //跳出循环
                flag=true;
                break;
            }
        }
        //商品不存在
        if (!flag)
        {
            //更据itemId取商品
            TbItem item = itemService.getItemById(itemId);
            //设置数量
            item.setNum(num);
            //取一张图片
            String image = item.getImage();
            if (StringUtils.isNotBlank(image))
            {
                item.setImage(image.split(",")[0]);
            }
            //把商品添加到商品列表
            list.add(item);

        }
        //写入Cooike
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(list),COOKIE_CART_TIME,true);
        //返回添加成功页面
        return "cartSuccess";


    }


    /**
     * 从cookie获取商品信息
     * @param request
     * @return
     */
    public List<TbItem> getCartListFormCookie(HttpServletRequest request){

        String json = CookieUtils.getCookieValue(request, "cart", true);
        //判断json是否为空
        if (!StringUtils.isNotBlank(json))
        {
            return new ArrayList<>();
        }
        //把json转换成商品列表对象
        List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
        return list;

    }


    /**
     * 显示购物车列表
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response) {
        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFormCookie(request);
        //判断用户是否为登录状态
        TbItem user = (TbItem) request.getAttribute("user");
        if (user != null) {
        //是
        //从cookie中取购物车列表
            if (cartList.size()>0)
            {
                //不为空 合并到服务端
                cartService.mergeCart(user.getId(), cartList);
                //把cookie中的购物车列表删除
                CookieUtils.deleteCookie(request,response,"cart");
                cartList = cartService.getCartList(user.getId());
            }
        //从服务端取购物车列表
        }
        //把列表传递给页面
        request.setAttribute("cartList", cartList);
        //返回逻辑视图
        return "cart";
    }

    /**
     * 更新购物车商品数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
                                HttpServletRequest request,HttpServletResponse response){
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        if (user!=null){
             cartService.updateCartNum(user.getId(), itemId, num);
            return E3Result.ok();
        }
        //从cookie中取购物车列表
        List<TbItem> list = getCartListFormCookie(request);
        for(TbItem item:list)
        {
            if (item.getId().longValue()==itemId)
            {
                //更新数量
                item.setNum(num);
                break;
            }
        }
        //把商品信息写入购物车
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(list),COOKIE_CART_TIME,true);
        //返回成功
        return E3Result.ok();
    }


    /**
     * 删除购物车商品
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){

        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        if (user!=null)
        {
            cartService.deleteCartItem(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        //从cookie中取购物车列表
        List<TbItem> list=getCartListFormCookie(request);
        //遍历商品 找到要删除的商品
        for (TbItem item:list)
        {
            if (item.getId().longValue()==itemId)
            {
                //删除商品
                list.remove(item);
                break;
            }
        }
        //把商品信息写入购物车cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(list),COOKIE_CART_TIME,true);
        return "redirect:/cart/cart.html";
    }


}
