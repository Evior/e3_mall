package cn.e3mall.search.service;

import cn.e3mall.comment.pojo.SearchResult;

public interface SearchService  {

    SearchResult search(String keywords,int page,int rows) throws Exception;

}
