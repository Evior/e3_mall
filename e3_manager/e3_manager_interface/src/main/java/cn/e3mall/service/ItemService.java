package cn.e3mall.service;

import cn.e3mall.comment.pojo.EasyUIDataGridResult;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

    //通过id查询
    TbItem getItemById(long itemId);
    //通过id查询商品描述
    TbItemDesc getItemDescById(long itemId);
    //查询所有
    EasyUIDataGridResult getItemList(int page,int rows);
    E3Result saveItem(TbItem item,String desc);

    E3Result getItemDescById(Long itemId);

    E3Result upateItem(TbItem tbItem, String desc);

    E3Result deleteItem(Long[] ids);

    E3Result itemInstock(Long[] ids);

    E3Result itemReshelf(Long[] ids);
}
