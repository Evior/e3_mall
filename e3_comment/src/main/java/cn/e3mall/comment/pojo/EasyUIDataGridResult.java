package cn.e3mall.comment.pojo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 用于传输分页信息的对象
 * 1 Serializable 用于网络传输的对象必须序列化
 * 2
 *
 */


public class EasyUIDataGridResult implements Serializable{

    //总记录的条数
    private long total;
    //记录
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
