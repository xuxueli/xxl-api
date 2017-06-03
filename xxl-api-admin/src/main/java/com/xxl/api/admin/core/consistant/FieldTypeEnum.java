package com.xxl.api.admin.core.consistant;

/**
 * Created by xuxueli on 17/6/2.
 */
public enum FieldTypeEnum {

    DEFAULT(0, "默认"),
    ARRAY(1, "数组");

    private int value;
    private String title;
    private FieldTypeEnum(int value, String title){
        this.value = value;
        this.title = title;
    }

    public int getValue() {
        return value;
    }
    public String getTitle() {
        return title;
    }

    public static FieldTypeEnum match(int value){
        for (FieldTypeEnum item: FieldTypeEnum.values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

}