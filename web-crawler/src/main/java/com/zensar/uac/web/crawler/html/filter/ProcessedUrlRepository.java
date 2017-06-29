package com.zensar.uac.web.crawler.html.filter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * Created by Sagar Balai on 08/30/16.
 * Purpose of the class: It will contain set of processed web site urls.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class ProcessedUrlRepository {
    Set<String> processedUrlSet = new HashSet<>();

    public void addProcessedUrl(String url) {
        if (processedUrlSet == null) {
            processedUrlSet = new HashSet<>();
        }
        if (url.charAt(url.length() - 1) == '/' || url.charAt(url.length() - 1) == '\\') {
            url = url.substring(0, url.length() - 1);
        }
        processedUrlSet.add(url);
    }

    public boolean isProcessed(String url) {
        if (url.charAt(url.length() - 1) == '/' || url.charAt(url.length() - 1) == '\\') {
            url = url.substring(0, url.length() - 1);
        }
        return processedUrlSet.contains(url);
    }

    public Set<String> getProcessedUrlSet() {
        return processedUrlSet;
    }
}
