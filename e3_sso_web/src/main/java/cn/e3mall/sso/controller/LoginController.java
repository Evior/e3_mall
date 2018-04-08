package cn.e3mall.sso.controller;

import cn.e3mall.comment.utils.CookieUtils;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model)
    {
        model.addAttribute("redirect",redirect);
        return "login";
    }




    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    @ResponseBody
    private E3Result userlogin(String username, String password, HttpServletRequest request, HttpServletResponse response){

        E3Result e3Result = loginService.userlogin(username, password);
        //判断是否登录成功
        if (e3Result.getStatus()==200)
        {
            String token = e3Result.getData().toString();
            //把token写入cookie
            CookieUtils.setCookie(request,response,TOKEN_KEY,token);
        }
        //返回结果
        return  e3Result;

    }
}
