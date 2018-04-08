package cn.e3mall.order.controller;

import cn.e3mall.car.service.CartService;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {


    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
     public String showOrderCart(HttpServletRequest request){
        //取用户id
        TbUser tbuser = (TbUser) request.getAttribute("user");
        //取购物车列表
        List<TbItem> cartList = cartService.getCartList(tbuser.getId());
        //购物车列表传到页面
        request.setAttribute("cartList",cartList);
        return "order-cart";
     }

     @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
         //取用户信息
         TbUser user = (TbUser) request.getAttribute("user");
         //把用户信息添加到orderInfo
         orderInfo.setUserId(user.getId());
         //调用服务生产订单
         E3Result e3Result = orderService.createOrder(orderInfo);
         //如果订单生成成功 清空购物车
         if (e3Result.getStatus()==200)
         {
             //清空购物车
             cartService.clearCartItem(user.getId());
         }
         //把订单号传递给页面
         request.setAttribute("orderId",e3Result.getData());
         request.setAttribute("payment",orderInfo.getPayment());
         //返回逻辑视图
         return "success";
     }


}
