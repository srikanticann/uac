package com.zensar.uac.web.crawler.services;

import com.zensar.uac.web.crawler.domain.WebCrawlerReport;

import java.util.List;

/**
 * Created by Sagar Balai on 10/16/2016.
 * Purpose of the class: ReportService is responsible to create WebCrawlerReport report for for givin URL
 * This is basic interface
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface ReportService {

    WebCrawlerReport generateReport(String URL, String emailAddress);

    int checkCrawlingUrl(String URL);
}
