package com.zensar.uac.web.crawler.util;

import com.zensar.uac.web.crawler.constants.CrawlerConstants;

/**
 * Created by Sagar Balai on 10/16/2016.
 * Purpose of the class: DomainUtil use to get the correct domain
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class DomainUtil {
    public static String getDomain(String url) {

        String httpStr = CrawlerConstants.HTTP + CrawlerConstants.COLON
                + CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER;
        String httpsStr = CrawlerConstants.HTTPS + CrawlerConstants.COLON
                + CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER;

        int firstIndex = 0;
        int lastIndex = url.length();

        if (url.contains(CrawlerConstants.WWW + CrawlerConstants.DOT)) {
            firstIndex = url.indexOf(CrawlerConstants.WWW
                    + CrawlerConstants.DOT)
                    + (CrawlerConstants.WWW + CrawlerConstants.DOT).length();
        } else {
            if (url.contains(httpStr)) {
                firstIndex = httpStr.length();
            } else {
                firstIndex = httpsStr.length();
            }
        }

        if (url.contains(CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER)
                && url.indexOf(
                CrawlerConstants.SINGLE_BACKSLASH_CHARACTER,
                url.indexOf(CrawlerConstants.SINGLE_BACKSLASH_CHARACTER) + 2) > 0) {
            lastIndex = url
                    .indexOf(
                            CrawlerConstants.SINGLE_BACKSLASH_CHARACTER,
                            url.indexOf(CrawlerConstants.SINGLE_BACKSLASH_CHARACTER) + 2);
        }

        String domain = url.substring(firstIndex, lastIndex);
        return domain;
    }

}
