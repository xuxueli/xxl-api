package com.xxl.api.admin.web.error;

import org.springframework.boot.web.error.ErrorPage;
import org.springframework.boot.web.error.ErrorPageRegistrar;
import org.springframework.boot.web.error.ErrorPageRegistry;
import org.springframework.stereotype.Component;

/**
 * error page
 */
@Component
public class WebErrorPageRegistrar implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {

        ErrorPage errorPage = new ErrorPage("/errorpage");
        registry.addErrorPages(errorPage);

        /*ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/errorpage");
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/errorpage");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/errorpage");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errorpage");
        registry.addErrorPages(error400Page,error401Page,error404Page,error500Page);*/
    }
}
