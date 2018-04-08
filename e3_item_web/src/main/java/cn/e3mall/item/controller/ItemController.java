package cn.e3mall.item.controller;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品详情页面Controller
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable long itemId, Model model){

        //取商品信息
        TbItem tbItem = itemService.getItemById(itemId);
        Item item=new Item(tbItem);
        //取描述信息
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        //把信息传递给页面
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";

    }




}
