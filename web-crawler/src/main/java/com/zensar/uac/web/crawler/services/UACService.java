package com.zensar.uac.web.crawler.services;

import com.zensar.uac.web.crawler.domain.WebCrawlerReport;

import java.util.List;

/**
 * Created by srikant.singh on 10/16/2016.
 * Purpose of the class: UACService is responsible to create crawling report for new URL or already processed URL's.
 * This is basic interface and exposed to external user and will act as starting point for this web crawler application.
 * <p>
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface UACService {

    WebCrawlerReport crawl(String URL, String emailID);

    List<WebCrawlerReport> generateReport(String inputURL);
}
