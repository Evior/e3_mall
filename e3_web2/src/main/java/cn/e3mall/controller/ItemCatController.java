package cn.e3mall.controller;

import cn.e3mall.comment.pojo.EasyUITreeNode;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类表现层
 *
 *
 *
 *
 *
 *
 */


@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;


    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatlist(@RequestParam(name = "id",defaultValue = "1") Long parent)
    {
        List<EasyUITreeNode> itemCatlist = itemCatService.getItemCatlist(parent);
        return itemCatlist;
    }



}
