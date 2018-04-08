package cn.e3mall.car.service.impl;

import cn.e3mall.car.service.CartService;
import cn.e3mall.comment.jedis.JedisClientCluster;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.comment.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;

    @Autowired
    private JedisClientCluster jedisClientCluster;

    @Autowired
    private TbItemMapper tbItemMapper;



    @Override
    public E3Result addCart(long userId, long itemId, int num) {
        //向redis中添加购物车信息
        Boolean hexists = jedisClientCluster.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        //数据类型为hash key 用户id  field:商品id value 商品信息
        if (hexists){
            String json = jedisClientCluster.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            //json转换为TbItem
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            item.setNum(item.getNum()+num);
            //写会redis
            jedisClientCluster.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
            return E3Result.ok();
        }
        //判断商品是否存在
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        //设置数量
        item.setNum(num);
        //取一张图片
        String image = item.getImage();
        if (StringUtils.isNotBlank(image))
        {
         item.setImage( image.split(",")[0]);
        }
        //添加购物车列表
        jedisClientCluster.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    @Override
    public E3Result mergeCart(long userId, List<TbItem> itemList) {


        for (TbItem item:itemList)
        {
            addCart(userId,item.getId(),item.getNum());
        }
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {
        //根据用户id查询用户列表
        List<String> stringList = jedisClientCluster.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> list=new ArrayList<>();
        for (String str:stringList)
        {
            TbItem item = JsonUtils.jsonToPojo(str, TbItem.class);
            //添加到列表
            list.add(item);
        }
        return list;
    }

    @Override
    public E3Result updateCartNum(long userId, long itemId, int num) {
        //从redis取商品信息
        String json = jedisClientCluster.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        //更新商品的num
        TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
        item.setNum(num);
        //写入redis
        jedisClientCluster.hset(REDIS_CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
        return E3Result.ok();
    }

    @Override
    public E3Result deleteCartItem(long userId, long itemId) {
        //删除
        jedisClientCluster.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result clearCartItem(long userId) {
        jedisClientCluster.hdel(REDIS_CART_PRE + ":" + userId);
        return E3Result.ok();
    }
}
