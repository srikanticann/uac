package com.zensar.uac.web.crawler.services.cache;

import com.zensar.uac.web.crawler.domain.WebCrawlerReport;

/**
 * Created by srikant.singh on 10/06/2016.
 * Purpose of the class: Basic interface for caching
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface ReportCache {

    void store(WebCrawlerReport report);

    WebCrawlerReport retrieve(String URL);
}
