package com.zensar.uac.web.crawler.core.database;

import java.util.List;

/**
 * Created by Sagar on 10/24/2016.
 * Purpose of the class: persist WebCrawlerData
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */

public interface EntityStoreManager {
    void store(WebCrawlerData data);

    WebCrawlerData retrieve(String URL);

    List<WebCrawlerData> retrieveAllEntity();
}
