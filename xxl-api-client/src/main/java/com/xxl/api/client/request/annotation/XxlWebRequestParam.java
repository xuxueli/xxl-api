package com.xxl.api.client.request.annotation;

import java.lang.annotation.*;

/**
 * Created by xuxueli on 17/5/26.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface XxlWebRequestParam {

    /**
     * 是否必填
     *
     * @return
     */
    boolean required() default false;

    /**
     * 默认值
     *
     * @return
     */
    String defaultValue() default "";

    /**
     * Date类型参数，格式化格式
     *
     * @return
     */
    String datePattern() default "yyyy-MM-dd HH:mm:ss";

}
