package com.zensar.uac.web.crawler.html;

import com.zensar.uac.web.crawler.pojo.EmailCrawlerReport;
import com.zensar.uac.web.crawler.util.ASCIIFormatChecker;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ssrikant.singh on 09/26/2016.
 * Purpose of the class: It will crawl 'inputURL' and give number of Emails which are
 * present on that HTML page
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class EmailCrawler {

    public EmailCrawlerReport crawl(Document doc) {
        int emailFieldCount = 0;
        Set<String> asciiEmails = new HashSet<String>();
        Set<String> emails = new HashSet<String>();
        Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = p.matcher(doc.text());
        while (matcher.find()) {
            if (ASCIIFormatChecker.check(matcher.group())) {
                asciiEmails.add(matcher.group());
            }
            emails.add(matcher.group());

        }
        emailFieldCount = getEmailsInputFieldsCount(doc);

        EmailCrawlerReport emailCrawlerReport = new EmailCrawlerReport(asciiEmails, emails, emailFieldCount);

        return emailCrawlerReport;
    }

    private int getEmailsInputFieldsCount(Document doc) {
        int count = 0;
        Outer:
        for (Element element : doc.getAllElements()) {
            Inner:
            for (Attribute attribute : element.attributes()) {
                if (!element.tagName().equals("label") && attribute.getKey().equalsIgnoreCase("email")) {
                    count = count + 1;
                    break Outer;
                } else if (attribute.getValue().equalsIgnoreCase("Enter your email")
                        || attribute.getValue().equalsIgnoreCase("Enter email")) {
                    count = count + 1;
                }
            }
        }
        return count;
    }
}