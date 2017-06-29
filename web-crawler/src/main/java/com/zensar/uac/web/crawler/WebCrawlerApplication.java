package com.zensar.uac.web.crawler;

import org.apache.tapestry5.spring.TapestrySpringFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.AbstractEnvironment;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Created by srikant.singh on 10/16/2016.
 * Purpose of the class: This is the entry point. This is a spring boot application.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan({"com.zensar.uac.web.crawler"})
public class WebCrawlerApplication implements ServletContextInitializer {

    public static void main(String[] args) {
        if (System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME) == null) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "production");
        }
        SpringApplication.run(WebCrawlerApplication.class, args);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        servletContext.setInitParameter("contextClass",
                "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");

        servletContext.setInitParameter("tapestry.app-package", "com.zensar.uac.web.crawler");
        servletContext.setInitParameter("tapestry.use-external-spring-context", "true");
        FilterRegistration.Dynamic filter = servletContext.addFilter("app", TapestrySpringFilter.class);
        filter.addMappingForUrlPatterns(null, true, "/*");
    }
}
