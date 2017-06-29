package com.zensar.uac.web.crawler.util;

import com.zensar.uac.web.crawler.constants.CrawlerConstants;

/**
 * Created by Sagar Balai on 04-10-2016.
 * Purpose of the class: Use  to generate correct urls
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class UrlUtil {

    /**
     * It will create correct web site URL, add missing 'http"://' or 'https://'
     *
     * @param inputUrl
     * @return
     */
    public static String getCorrectHttpUrl(String inputUrl) {
        String url;
        if (!inputUrl.startsWith(CrawlerConstants.HTTP + CrawlerConstants.COLON
                + CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER)) {
            if (!inputUrl.startsWith(CrawlerConstants.HTTPS
                    + CrawlerConstants.COLON
                    + CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER)) {
                url = CrawlerConstants.HTTP + CrawlerConstants.COLON
                        + CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER
                        + inputUrl;
            } else {
                url = inputUrl;
            }
        } else {
            url = inputUrl;
        }
        if (url.endsWith("/")) {
            StringBuffer tempUrl = new StringBuffer("");
            char[] charArray = url.toCharArray();
            for (int i = 0; i < charArray.length - 1; i++) {
                tempUrl.append(charArray[i]);
            }

            url = tempUrl.toString();
        }

        return url;
    }

}
