package cn.e3mall.comment.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    private long recoordCount;
    private int totalPages;
    private List<SearchItem> itemList;

    public long getRecoordCount() {
        return recoordCount;
    }

    public void setRecoordCount(long recoordCount) {
        this.recoordCount = recoordCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
}
