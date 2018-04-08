package cn.e3mall.content.service.impl;

import cn.e3mall.comment.jedis.JedisClientCluster;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.comment.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    //注入依赖
    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClientCluster jedisClientCluster;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;
    @Override
    public E3Result addContent(TbContent content) {

        //补全
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到数据库
        contentMapper.insert(content);
        //缓存同步
        jedisClientCluster.hdel(CONTENT_LIST,content.getCategoryId()+"");
        return E3Result.ok();
    }

    /**
     * 根据内容id查询内容列表
     * @param cid
     * @return
     */
    @Override
    public List<TbContent> getContentListByCid(long cid) {

         try {
             //查询缓存
             String json = jedisClientCluster.hget(CONTENT_LIST, cid + "");
             //如果缓存中有直接响应
             if (StringUtils.isNotBlank(json))
             {
                 List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                 return list;
             }
             //如果缓存中没有查询数据库
         }catch (Exception e)
         {
             e.printStackTrace();
         }


        TbContentExample example=new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
        //把查询结果写入缓存
        try {
           jedisClientCluster.hset(CONTENT_LIST,Long.toString(cid), JsonUtils.objectToJson(list));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return list;
    }
}
