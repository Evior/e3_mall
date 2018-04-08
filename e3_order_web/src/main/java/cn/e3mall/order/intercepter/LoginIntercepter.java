package cn.e3mall.order.intercepter;

import cn.e3mall.car.service.CartService;
import cn.e3mall.comment.utils.CookieUtils;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.comment.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginIntercepter implements HandlerInterceptor {


    //SSO_URL
    @Value("${SSO_URL}")
    private String SSO_URL;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //从cookie中取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        //判断token是否存在
        if (StringUtils.isBlank(token))
        {
            //不存在
            //跳转到登录页面  当前页面做数据传递
            httpServletResponse.sendRedirect(SSO_URL+"/page/login?redirect="+httpServletRequest.getRequestURL());
            //拦截
            return  false;
        }
        //存在
        E3Result e3Result = tokenService.getUserByToken(token);
        //判断token是否过期
        if (e3Result.getStatus()!=200)
        {
            //过期
            //跳转到登录页面  当前页面做数据传递
            httpServletResponse.sendRedirect(SSO_URL+"/page/login?redirect="+httpServletRequest.getRequestURL());
            //拦截
            return  false;
        }
       //未过期 登录状态
        TbUser tbuser = (TbUser) e3Result.getData();
        httpServletRequest.setAttribute("user",tbuser);
        //判断cookie中是否有购物车数据，如果有就要合并
        String json = CookieUtils.getCookieValue(httpServletRequest, "cart", true);
        if (StringUtils.isNotBlank(json))
        {
            //json不为空
            //合并购物车
            cartService.mergeCart(tbuser.getId(), JsonUtils.jsonToList(json, TbItem.class));
        }

        //放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
