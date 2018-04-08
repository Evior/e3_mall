package cn.e3mall.controller;

import cn.e3mall.comment.pojo.EasyUIDataGridResult;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.pojo.TbItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	//测试
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}

	//分页显示列表
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//调用服务查询商品列表
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}

	//新增
	@RequestMapping("/item/save")
	@ResponseBody()
	public E3Result getItemList(TbItem tbItem, String desc) {
		E3Result result = itemService.saveItem(tbItem, desc);
		return result;

	}

	//编辑
	@RequestMapping("/rest/page/item-edit")
	public String getEdit(Model model)
	{
		return "redirect:/item-edit";
	}


	//更新商品
	@RequestMapping("/rest/item/update")
	@ResponseBody()
	public E3Result updateItem(TbItem tbItem, String desc) {
		E3Result result = itemService.upateItem(tbItem, desc);
		return result;

	}

	//删除商品
	@RequestMapping("/rest/item/delete")
	@ResponseBody()
	public E3Result deleteItem(Long[] ids) {
		E3Result result = itemService.deleteItem(ids);

		return result;

	}


	//下架
	@RequestMapping("/rest/item/instock")
	@ResponseBody()
	public E3Result itemInstock(Long[] ids) {
		E3Result result = itemService.itemInstock(ids);
		return result;
	}


	//下架
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody()
	public E3Result itemReshelf(Long[] ids) {
		E3Result result = itemService.itemReshelf(ids);
		return result;
	}



	//编辑@PathVariable Long itemId
	//http://localhost:8081/rest/item/query/item/desc/536563
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result getEditDesc(@PathVariable Long itemId)
	{

		E3Result e3Result = itemService.getItemDescById(itemId);


		return e3Result;
	}






}
