package cn.e3mall.search.message;

import cn.e3mall.comment.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听商品添加信息，接收消息后，将对应的商品同步到索引库
 */
public class ItemAddMessageListener implements MessageListener{

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;
    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品id
            TextMessage textMessage= (TextMessage) message;
            String text = textMessage.getText();
            Long itemId=new Long(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品id 获取商品信息
            SearchItem item = itemMapper.getItemById(itemId);
            //创建一个文档对象
            SolrInputDocument document=new SolrInputDocument();
            //向文档添加域
            document.addField("id",item.getId());
            document.addField("item_title",item.getTitle());
            document.addField("item_sell_point",item.getSell_point());
            document.addField("item_price",item.getPrice());
            document.addField("item_image",item.getImages());
            document.addField("item_category_name",item.getCategory_name());
            //把文档写入索引库
            solrServer.add(document);
            //提交事务
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
