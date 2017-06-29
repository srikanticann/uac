package com.zensar.uac.web.crawler.services;

import com.zensar.uac.web.crawler.domain.WebCrawlerReport;

/**
 * Created by srikant.singh on 10/16/2016.
 * Purpose of the class: Crawler is responsible for crawling the given URL and return crawler report which inclues all criteria which impacts complexity of URL like
 * 1. All URL  in page with ASCII code
 * 2  1. All URL  in page with ASCII code with utf charset
 * 3. Static links
 * 4. Double byte characters
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */

public interface UACWebCrawler {
    WebCrawlerReport crawl(String inputURL, String emailID);
}
