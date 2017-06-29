package com.zensar.uac.web.crawler.html.filter;

import com.zensar.uac.web.crawler.util.DomainUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sagar Balai on 08/30/16.
 * Purpose of the class: It will filter URL on the basis of 'allowedDomain' and
 * skipDomains.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class DomainFilter {
    private String domain;
    private String location;
    private Set<String> skipDomainss;
    private Set<String> processedUrls = new HashSet<String>();

    public synchronized void addProcessedUrl(String url) {
        processedUrls.add(url);
    }

    public String getDomain() {
        return domain;
    }

    public synchronized void setDomain(String domain) {
        this.domain = domain;
    }

    public Set<String> getSkipDomainss() {
        return skipDomainss;
    }

    public synchronized void setSkipDomainss(Set<String> skipDomainss) {
        this.skipDomainss = skipDomainss;
    }

    public synchronized boolean doFilter(String websiteUrl) {
        boolean isDomainUrl = false;
        if (websiteUrl.startsWith(location)) {
            isDomainUrl = true;
        }
        return isDomainUrl;
    }

    public Set<String> doFilter(Set<String> urlSet) {
        Set<String> domainUrlSet = null;
        Set<String> externalSet = new HashSet<>();
        for (String url : urlSet) {
            if (url.startsWith(location)) {
                String urlDomain = DomainUtil.getDomain(url);
                if (url.contains(domain) && urlDomain.equalsIgnoreCase(domain)) {
                    if (domainUrlSet == null) {
                        domainUrlSet = new HashSet<String>();
                    }
                    domainUrlSet.add(url);
                } else {
                    externalSet.add(url);
                }
            } else {
                externalSet.add(url);
            }
        }
        return domainUrlSet;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
