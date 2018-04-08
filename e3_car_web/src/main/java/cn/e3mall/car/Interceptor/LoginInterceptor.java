package cn.e3mall.car.Interceptor;

import cn.e3mall.comment.utils.CookieUtils;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断是否登录的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //从cookie取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        //如果没有token未登录  直接放行
        if (StringUtils.isNotBlank(token))
        {
            return true;
        }
        //取token 调用sso系统的服务 根据token取用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //没用取到用户信息 登录过期 直接放行
        if (e3Result.getStatus()!=200)
        {
            return true;
        }
        //取用户的信息 登录状态
        TbUser user = (TbUser) e3Result.getData();
        //把信息放入request中
        httpServletRequest.setAttribute("user",user);
        return false;





    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
