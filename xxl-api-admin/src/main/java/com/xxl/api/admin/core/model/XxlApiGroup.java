package com.xxl.api.admin.core.model;

/**
 * Created by xuxueli on 17/3/30.
 */
public class XxlApiGroup {

    private int id;
    private int productId;  // 项目ID
    private String name;    // 分组名称
    private int order;      // 分组排序

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
