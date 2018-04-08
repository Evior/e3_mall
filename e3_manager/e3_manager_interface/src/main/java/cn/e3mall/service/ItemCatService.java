package cn.e3mall.service;

import cn.e3mall.comment.pojo.EasyUITreeNode;

import java.util.List;



/**
 *
 *     商品分类:初始化数节点的接口
 *
 *
 */
public interface ItemCatService {

    List<EasyUITreeNode> getItemCatlist(long parentId);
}
