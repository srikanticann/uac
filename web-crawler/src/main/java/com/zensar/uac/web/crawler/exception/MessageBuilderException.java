package com.zensar.uac.web.crawler.exception;

/**
 * Created by srikant.singh on 10/12/2016.
 * Purpose of the class: Exception for Messgin building
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class MessageBuilderException extends Exception {
    public MessageBuilderException(Exception e) {
        super(e);
    }

    public MessageBuilderException(String msg) {
        super(msg);
    }
}
