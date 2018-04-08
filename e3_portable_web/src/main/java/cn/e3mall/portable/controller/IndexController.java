package cn.e3mall.portable.controller;


import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Value("${COMTENT_LUBOTU}")
    private Long COMTENT_LUBOTU;

    @Autowired
    private ContentService contentService;
    @RequestMapping("/index")
    public String showIndex(Model model){

       //查询内容列表
        List<TbContent> ad1List = contentService.getContentListByCid(COMTENT_LUBOTU);
        //把属性传递给页面
        model.addAttribute("ad1List",ad1List);
        return "index";
    }


}
