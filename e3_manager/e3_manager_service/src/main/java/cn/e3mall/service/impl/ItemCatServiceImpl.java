package cn.e3mall.service.impl;

import cn.e3mall.comment.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    //注入依赖
    @Autowired
    private TbItemCatMapper tbItemCatMapper;


    /**
     * 获取分类列表初始化树的节点集合
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUITreeNode> getItemCatlist(long parentId) {

        //实例化分类列表模板
        TbItemCatExample example=new TbItemCatExample();
        //创建条件查询对象
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //添加条件
        criteria.andParentIdEqualTo(parentId);
        //通过模板查询
        List<TbItemCat> itemCatList = tbItemCatMapper.selectByExample(example);
        //新建一个分类列表对象集合对象
        List<EasyUITreeNode> list=new ArrayList<EasyUITreeNode>();
        //遍历填充
        for (TbItemCat tbItemCat:itemCatList)
        {
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            list.add(node);
        }
        return list;
    }
}
