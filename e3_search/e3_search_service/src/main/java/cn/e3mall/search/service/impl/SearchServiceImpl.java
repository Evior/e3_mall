package cn.e3mall.search.service.impl;

import cn.e3mall.comment.pojo.SearchResult;
import cn.e3mall.search.Dao.SearchDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品搜索
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    @Override
    public SearchResult search(String keywords, int page, int rows) throws Exception {
        //创建SolrQuery对象
        SolrQuery solrQuery=new SolrQuery();
        //设置查询条件
        solrQuery.setQuery(keywords);
        //设置分页显示
        if (page<=0)
        {
            page=1;
        }
        solrQuery.setStart((page-1)*rows);
        solrQuery.setRows(rows);
        //设置默认搜索域
        solrQuery.set("df","item_title");
        //开启高亮显示
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");
        //调用dao
        SearchResult result = searchDao.search(solrQuery);
        //计算总页数
        long recoordCount = result.getRecoordCount();

        int totalPage=(int)(recoordCount/rows);
        if (recoordCount%rows>0)
        {
            totalPage++;
        }
        result.setTotalPages(totalPage);
        //返回结果
        return result;
    }
}
