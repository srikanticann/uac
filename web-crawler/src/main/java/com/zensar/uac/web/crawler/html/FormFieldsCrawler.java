package com.zensar.uac.web.crawler.html;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by srikant.singh on 11/18/2016.
 */

/**
 * Created by Sagar Balai on 08/30/16.
 * Purpose of the class: It will crawl 'inputURL' and give number of Form Fields which are
 * present on that HTML page
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class FormFieldsCrawler {

    public int crawl(Document doc) {
        int count = 0;
        int mainCount = 0;
        Outer:
        for (Element element1 : doc.getElementsByTag("form")) {
            for (Element element : element1.getAllElements()) {
                Inner:
                if (element.tagName().equals("input")) {
                    for (Attribute attribute : element.attributes()) {
                        final boolean typeIsKey = attribute.getKey().equalsIgnoreCase("type");
                        if ((typeIsKey && attribute.getValue().equalsIgnoreCase("text"))
                                || (typeIsKey && attribute.getValue().equalsIgnoreCase("email"))
                                || (typeIsKey && attribute.getValue().equalsIgnoreCase("password"))
                                || (typeIsKey && attribute.getValue().equalsIgnoreCase("tel"))) {
                            count = 1;
                        }
                    }
                    mainCount += count;
                    count = 0;
                }
            }
        }
        return mainCount;
    }
}
