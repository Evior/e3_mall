package cn.e3mall.service.impl;

import cn.e3mall.comment.jedis.JedisClientCluster;
import cn.e3mall.comment.pojo.EasyUIDataGridResult;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.comment.utils.IDUtils;
import cn.e3mall.comment.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemDescExample;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;
    /*根据id注入*/
    @Resource
    private Destination topicDestination;

    /*redis缓存*/
    @Autowired
    private JedisClientCluster jedisClientCluster;

    /*redis存储前缀*/
    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

    /*过期时间*/
    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    @Override
    public TbItem getItemById(long itemId) {

        //查询缓存
        try {

            String json = jedisClientCluster.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json))
            {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }


        //缓存中没有查询数据库

        //根据主键查询
        //TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            //把结果添加到缓存
            try {
                jedisClientCluster.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
                //设置过期时间
                jedisClientCluster.expire(REDIS_ITEM_PRE+":"+itemId+":BASE",ITEM_CACHE_EXPIRE);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return list.get(0);
        }
        return null;
    }

    @Override
    public TbItemDesc getItemDescById(long itemId) {
        //查询缓存
        try {

            String json = jedisClientCluster.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json))
            {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

        TbItemDescExample example=new TbItemDescExample();
        TbItemDescExample.Criteria criteria=example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemDesc> tbItemDescList = itemDescMapper.selectByExample(example);
        if (tbItemDescList != null && tbItemDescList.size() > 0) {
            //把结果添加到缓存
            try {
                jedisClientCluster.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(tbItemDescList.get(0)));
                //设置过期时间
                jedisClientCluster.expire(REDIS_ITEM_PRE+":"+itemId+":DESC",ITEM_CACHE_EXPIRE);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return tbItemDescList.get(0);
        }
        return null;
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //取分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //取总记录数
        long total = pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }

    @Override
    public E3Result saveItem(TbItem item, String desc) {
        //生成产品id
        final long id = IDUtils.genItemId();
        //封装item 部分对应的框架已经封装
        item.setId(id);
        //添加状态
        item.setStatus((byte)1);
        //添加创建时间
        Date date=new Date();
        item.setCreated(date);
        //添加修改时间
        item.setUpdated(date);
        //向商品表插入数据
        itemMapper.insert(item);
        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
       // 6、向商品描述表插入数据
        itemDescMapper.insert(tbItemDesc);

        //7、发送商品添加信息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(id + "");
                return message;
            }
        });

        // 8、E3Result.ok()
        return E3Result.ok();

    }


    public E3Result getItemDescById(Long id){
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(id);
        if (tbItemDesc!=null)
        {
            return new E3Result(200,"ok" ,tbItemDesc);
        }else {
            return new E3Result(404,"no",null);
        }


    }

    @Override
    public E3Result upateItem(TbItem tbItem, String desc) {


        //添加状态
        tbItem.setStatus((byte)1);
        //添加创建时间
        Date date=new Date();
        tbItem.setCreated(date);
        //添加修改时间
        tbItem.setUpdated(date);
        //向商品表插入数据
        itemMapper.updateByPrimaryKey(tbItem);
        TbItemDesc tbItemDesc=new TbItemDesc();
        tbItemDesc.setItemId(tbItem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        // 6、向商品描述表插入数据
        itemDescMapper.updateByPrimaryKey(tbItemDesc);
        // 7、E3Result.ok()
        return E3Result.ok();
    }

    @Override
    public E3Result deleteItem(Long[] ids) {
        for (Long id:ids
             ) {
            itemMapper.deleteByPrimaryKey(id);
        }
        return E3Result.ok();
    }

    //商品下架
    @Override
    public E3Result itemInstock(Long[] ids) {
        for(Long id:ids)
        {
            TbItem tbItem = itemMapper.selectByPrimaryKey(id);
            tbItem.setStatus((byte) 2);
            itemMapper.updateByPrimaryKey(tbItem);
        }
        return E3Result.ok();
    }


    //商品上架
    @Override
    public E3Result itemReshelf(Long[] ids) {
        for(Long id:ids)
        {
            TbItem tbItem = itemMapper.selectByPrimaryKey(id);
            tbItem.setStatus((byte) 1);
            itemMapper.updateByPrimaryKey(tbItem);
        }
        return E3Result.ok();
    }
}
