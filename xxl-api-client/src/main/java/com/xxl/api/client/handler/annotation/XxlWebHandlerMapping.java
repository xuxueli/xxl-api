package com.xxl.api.client.handler.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by xuxueli on 16/9/14.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface XxlWebHandlerMapping {

    // mapping value
    String value();

}
