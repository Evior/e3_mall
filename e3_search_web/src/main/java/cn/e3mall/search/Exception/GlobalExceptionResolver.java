package cn.e3mall.search.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalExceptionResolver implements HandlerExceptionResolver {
    private static  final Logger logger= LoggerFactory.getLogger(GlobalExceptionResolver.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse, Object o, Exception e) {

        //打印控制台
        e.printStackTrace();
        //写日志
        logger.info("测试的日志");
        //邮件短信
        //跳转到错误页面
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("error/exception");

        return modelAndView;
    }
}