package cn.e3mall.search.Dao;

import cn.e3mall.comment.pojo.SearchItem;
import cn.e3mall.comment.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDao {


    @Autowired
    private SolrServer solrServer;
    public SearchResult search(SolrQuery query) throws Exception{
        //根据查询条件查询索引库
        QueryResponse queryResponse=solrServer.query(query);
        //取查询结果
        SolrDocumentList documents = queryResponse.getResults();
        //去查询结果总记录数
        long numFound = documents.getNumFound();
        SearchResult searchResult = new SearchResult();
        searchResult.setRecoordCount(numFound);

        //取商品的列表 需要高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        List<SearchItem> itemList=new ArrayList<>();
        for (SolrDocument solrDocument:documents)
        {
            SearchItem item = new SearchItem();
            item.setId((String) solrDocument.get("id"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setImages((String) solrDocument.get("item_image"));
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title="";
            //取高亮显示
            if (list!=null&&list.size()>0)
            {
                title=list.get(0);
            }else {
               title= (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            item.setPrice((Long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            itemList.add(item);
        }
        searchResult.setItemList(itemList);
        //返回结果
        return searchResult;

    }

}
