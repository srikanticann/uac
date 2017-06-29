package com.zensar.uac.web.crawler.exception;

/**
 * Created by Sagar Balai on 08-09-2016.
 * Purpose of the class: Exception for URL Fetching
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class URLFetchingException extends RuntimeException {
    public URLFetchingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
