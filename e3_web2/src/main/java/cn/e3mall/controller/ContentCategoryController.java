package cn.e3mall.controller;

import cn.e3mall.comment.pojo.EasyUITreeNode;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {

    //注入依赖
    @Autowired
    private ContentCategoryService categoryService;


    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCatList(@RequestParam(name = "id",defaultValue = "0")Long parentId){
        List<EasyUITreeNode> list = categoryService.getContentCateList(parentId);
        return list;
    }


    @RequestMapping(value = "/content/category/create" ,method = RequestMethod.POST)
    @ResponseBody
    public E3Result createContentCategory(Long parentId,String name){

        //调用服务
        E3Result e3Result=categoryService.addContentCategory(parentId,name);
        return e3Result;
    }

}
