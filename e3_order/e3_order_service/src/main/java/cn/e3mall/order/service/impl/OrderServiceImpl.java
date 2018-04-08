package cn.e3mall.order.service.impl;

import cn.e3mall.comment.jedis.JedisClientCluster;
import cn.e3mall.comment.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

/**
 * 订单服务处理
 */
public class OrderServiceImpl implements OrderService {


    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Autowired
    private JedisClientCluster jedisClientCluster;

    @Value("${ORDER_ID_GEN}")
    private String ORDER_ID_GEN;

    @Value("${ORDER_ID_BEGIN}")
    private String ORDER_ID_BEGIN;
    @Value("${ORDER_DETAIL_GEN_KEY}")
    private String ORDER_DETAIL_GEN_KEY;


    /**
     * 新增订单
     * @param orderInfo
     * @return
     */
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //生成订单号 使用redis
        if (jedisClientCluster.exists(ORDER_ID_GEN)!=null)
        {
            //给索引初始值
            jedisClientCluster.set(ORDER_ID_GEN,ORDER_ID_BEGIN);
        }
        String orderId = jedisClientCluster.incr(ORDER_ID_GEN).toString();
        //补全属性
        //1、未付款  2、已发款  3、未付款  4、已发货  5、交易成功  6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //插入订单表
        orderMapper.insert(orderInfo);
        //向订单明细表插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem:orderItems){
            //订单项详情id
            String orderItemId = jedisClientCluster.incr(ORDER_DETAIL_GEN_KEY).toString();
            //补全属性
            tbOrderItem.setId(orderItemId);
            tbOrderItem.setItemId(orderId);
            //向明细表中插入数据
            orderItemMapper.insert(tbOrderItem);
        }
        //向订单物流表插入数据
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);
        //返回E3Result 包含订单号
        return E3Result.ok(orderId);
    }
}
