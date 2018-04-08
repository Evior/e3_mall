package cn.e3mall.content.service.impl;

import cn.e3mall.comment.pojo.EasyUITreeNode;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    //注入依赖
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;
    @Override
    public List<EasyUITreeNode> getContentCateList(long parentId) {
        //生成模板对象
        TbContentCategoryExample example=new TbContentCategoryExample();
        //查询对象
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //添加查询条件
        criteria.andParentIdEqualTo(parentId);
        //通过模板查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        //声明List<EasyUITreeNode>
        List<EasyUITreeNode> nodeList=new ArrayList<>();
        //封装List<EasyUITreeNode>
        for(TbContentCategory category:list)
        {
            EasyUITreeNode node=new EasyUITreeNode();
            node.setId(category.getId());
            node.setState(category.getIsParent()?"closed":"open");
            node.setText(category.getName());
            nodeList.add(node);
        }
        return nodeList;
    }

    @Override
    public E3Result addContentCategory(long parentId, String name)  {
        //创建POJO
        TbContentCategory tbContentCategory=new TbContentCategory();
        //设置属性
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        //1是正常 2删除
        tbContentCategory.setStatus(1);
        //默认排序是1
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setIsParent(false);
        //插入数据库
        contentCategoryMapper.insert(tbContentCategory);
        //判断父节点的isParent节点 如果不是true改为True
        //根据父节点的id查询父节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent())
        {
            parent.setIsParent(true);
            //更新到数据库
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果 包含id
        return E3Result.ok(tbContentCategory);
    }
}
