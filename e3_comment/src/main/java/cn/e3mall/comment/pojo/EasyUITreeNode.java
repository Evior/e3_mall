package cn.e3mall.comment.pojo;

import java.io.Serializable;

/**
 *
 *   用于初始化数组件的对象
 *
 */

public class EasyUITreeNode implements Serializable{

    //树id
    private Long id;
    //数的文本:用于显示
    private String text;
    //树的状态 :close  open
    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
