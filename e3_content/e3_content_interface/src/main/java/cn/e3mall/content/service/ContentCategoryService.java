package cn.e3mall.content.service;

import cn.e3mall.comment.pojo.EasyUITreeNode;
import cn.e3mall.comment.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCateList(long parentId);
    E3Result addContentCategory(long parentId,String name);
}
