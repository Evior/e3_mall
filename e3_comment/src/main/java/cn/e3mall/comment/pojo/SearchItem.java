package cn.e3mall.comment.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable{
    private String id;
    private String title;
    private String sell_point;
    private String images;
    private long price;
    private String category_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String[] getImage(){

        if(images!=null&&"".equals(images))
        {
            String[] strings=images.split(",");
            return strings;
        }else {
            return null;
        }
    }

}
