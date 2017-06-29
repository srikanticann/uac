package com.zensar.uac.web.crawler.html;

import com.zensar.uac.web.crawler.constants.CrawlerConstants;
import com.zensar.uac.web.crawler.dao.CrawledUrlRepository;
import com.zensar.uac.web.crawler.dao.CrawlingUrlInfoRepository;
import com.zensar.uac.web.crawler.domain.WebCrawlerReport;
import com.zensar.uac.web.crawler.exception.URLFetchingException;
import com.zensar.uac.web.crawler.html.filter.DomainFilter;
import com.zensar.uac.web.crawler.html.filter.ProcessedUrlRepository;
import com.zensar.uac.web.crawler.model.CrawledUrl;
import com.zensar.uac.web.crawler.model.CrawlingURLInfo;
import com.zensar.uac.web.crawler.pojo.EmailCrawlerReport;
import com.zensar.uac.web.crawler.pojo.URLCrawlerReport;
import com.zensar.uac.web.crawler.util.ASCIIFormatChecker;
import com.zensar.uac.web.crawler.util.DomainUtil;
import com.zensar.uac.web.crawler.util.URLToWebCrawlerReportConverter;
import com.zensar.uac.web.crawler.util.UrlUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.IDN;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Sagar Balai on 08/30/16.
 * Purpose of the class: It will crawl 'inputURL' and give number of URL's which are
 * present on that HTML page as well as all internal pages up to nth
 * layer.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */


@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class URLCrawler {

    @Autowired
    private CrawledUrlRepository crawledUrlRepository;
    @Autowired
    private CrawlingUrlInfoRepository crawlingUrlInfoRepository;
    private DomainFilter domainFilter = new DomainFilter();
    private ProcessedUrlRepository processedUrl = new ProcessedUrlRepository();
    private EmailCrawler emailCrawler = new EmailCrawler();
    private FormFieldsCrawler fromFieldCrawler = new FormFieldsCrawler();
    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    /**
     * Crawl input web site URL and it will return WebCrawlerReport which has
     * information of all internal links, external links. It will crawl all
     * links which are present on given URL page and all links which are present
     * on internal links pages. This is done for nth level and internally used
     * DFS algorithm to calculate report for give web site URL.
     *
     * @param inputUrl : web site URL for which web crawler report needs to be
     *                 calculated.
     * @return : WebCrawlerReport - report with all impacting measures.
     */
    public WebCrawlerReport crawl(String inputUrl) {
        //Document doc = null;
        URLCrawlerReport report = null;
        int inactiveAsciiLinkCount = 0;
        int inactiveLinkCount = 0;
        int domainAsciiLinks = 0;
        int domainAsciUtfLinks = 0;
        int domainLinks = 0;
        int extAsciiLinks = 0;
        int extLinks = 0;
        int formFieldCount = 0;
        Set<String> asciiEmails = new LinkedHashSet<>();
        Set<String> emails = new LinkedHashSet<>();
        Set<String> domainLinksSet = new LinkedHashSet<String>();
        processedUrl.getProcessedUrlSet().clear();

        /*String url = UrlUtil.getCorrectHttpUrl(inputUrl);
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            url = CrawlerConstants.HTTPS + CrawlerConstants.COLON
                    + CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER + inputUrl;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e1) {
                throw new URLFetchingException(
                        "There is issue in fetching give input URL :'"
                                + inputUrl
                                + "'. Please try with active valid URL.", e1)
            }
        }*/

        String domain = DomainUtil.getDomain(inputUrl);
        domainFilter.setDomain(domain);
        domainFilter.setLocation(inputUrl);


        CrawlingURLInfo crawlingURLInfo = crawlingUrlInfoRepository.findByCrawlingUrl(inputUrl);

        try {
            queue.put(inputUrl);
            while (!queue.isEmpty() && domainAsciiLinks < 25001) {
                report = getHTMLPageReport(queue.take(), domainLinksSet, crawlingURLInfo);
                if (report != null) {
                    domainAsciiLinks += report
                            .getDomainAsciiLinkCount();
                    domainAsciUtfLinks += report.getDomainAsciiUtfLinkCount();
                    domainLinks += report.getDomainLinkCount();
                    extAsciiLinks += report.getExtAsciiLinkCount();
                    extLinks += report.getExtLinkCounnt();
                    inactiveAsciiLinkCount += report.getInactiveAsciiLinkCount();
                    inactiveLinkCount += report.getInactiveLinkCount();

                    // email info
                    formFieldCount += report.getFormFieldCount();
                    emails.addAll(report.getEmails());
                    asciiEmails.addAll(report.getAsciiEmails());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pushData(domainLinksSet, crawlingURLInfo);
        report = new URLCrawlerReport();
        report.setDomainAsciiLinkCount(domainAsciiLinks);
        report.setDomainAsciiUtfLinkCount(domainAsciUtfLinks);
        report.setDomainLinkCount(domainLinks);
        report.setExtAsciiLinkCount(extAsciiLinks);
        report.setExtLinkCounnt(extLinks);
        report.setInactiveAsciiLinkCount(inactiveAsciiLinkCount);
        report.setInactiveLinkCount(inactiveLinkCount);
        report.setFormFieldCount(formFieldCount);
        report.setEmails(emails);
        report.setAsciiEmails(asciiEmails);

        WebCrawlerReport crawlerReport = URLToWebCrawlerReportConverter.convertURLReportToWebCrawlerReport(
                inputUrl, report);
        return crawlerReport;
    }

    private URLCrawlerReport getHTMLPageReport(String websiteUrl, Set<String> domainLinksSet, CrawlingURLInfo crawlingURLInfo) {
        URLCrawlerReport urlReport = null;

        if (!processedUrl.isProcessed(websiteUrl)) {
            processedUrl.addProcessedUrl(websiteUrl);
            int inactiveAsciiLinkCount = 0;
            int inactiveLinkCount = 0;
            int totalLinkInPage = 0;
            int domainAsciiLinks = 0;
            int domainAsciiLinksUtf = 0;
            int domainLinks = 0;
            int extAsciiLinks = 0;
            int extLinks = 0;
            int formFields = 0;

            // email info
            Set<String> asciiEmails = new LinkedHashSet<>();
            Set<String> emails = new LinkedHashSet<>();
            //int emailFieldCount = 0;

            Map<String, Integer> urlToLinCountkMap = null;
            Document document = null;
            Connection.Response response = null;
            if (domainFilter.doFilter(websiteUrl)) {
                try {
                    if (!ASCIIFormatChecker.check(websiteUrl)) {
                        websiteUrl = IDN.toASCII(websiteUrl);
                        try {
                            document = Jsoup.connect(websiteUrl).get();

                        } catch (Exception e) {
                            response = Jsoup.connect(websiteUrl)
                                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                                    .timeout(10000)
                                    .execute();
                            document = response.parse();
                        }
                        websiteUrl = IDN.toUnicode(websiteUrl);
                    } else {
                        try {
                            document = Jsoup.connect(websiteUrl).get();
                            if ("UTF-8".equalsIgnoreCase(document.charset().name())
                                    || "UTF-16".equalsIgnoreCase(document.charset().name())) {
                                domainAsciiLinksUtf++;
                            }
                        } catch (Exception e) {
                            response = Jsoup.connect(websiteUrl)
                                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                                    .timeout(10000)
                                    .execute();
                            document = response.parse();
                            if ("UTF-8".equalsIgnoreCase(document.charset().name())
                                    || "UTF-16".equalsIgnoreCase(document.charset().name())) {
                                domainAsciiLinksUtf++;
                            }
                        }
                    }
                } catch (Exception e1) {
                    if (ASCIIFormatChecker.check(websiteUrl)) {
                        inactiveAsciiLinkCount++;
                    }
                    inactiveLinkCount++;
                }
                if (document != null) {
                    if (ASCIIFormatChecker.check(websiteUrl)) {
                        domainAsciiLinks++;
                        domainLinksSet.add(websiteUrl);
                        if (domainLinksSet.size() > 100) {
                            pushData(domainLinksSet, crawlingURLInfo);
                            domainLinksSet.clear();
                        }
                    }
                    domainLinks++;

                    Elements links = document.select("a[href]");
                    totalLinkInPage = links.size();

                    urlToLinCountkMap = new HashMap<>();
                    urlToLinCountkMap.put(websiteUrl, totalLinkInPage);

                    // email stuff
                    EmailCrawlerReport emailReport = emailCrawler.crawl(document);
                    formFields = fromFieldCrawler.crawl(document);

                    emails = emailReport.getEmails();
                    asciiEmails = emailReport.getAsciiEmails();

                    for (Element link : links) {
                        if (link.attr("href").equalsIgnoreCase("#")) {
                            continue;
                        }
                        String linkStr = link.attr("abs:href");
                        if (linkStr == null || (linkStr != null && linkStr.isEmpty()) || linkStr.equals("#")) {
                            continue;
                        }
                        if (linkStr.charAt(0) == '/') {
                            linkStr = websiteUrl + linkStr;
                        }
                        try {
                            if (!queue.contains(linkStr)) {
                                queue.put(linkStr);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /*URLCrawlerReport report = getHTMLPageReport(linkStr, domainLinksSet);
                        if (report != null) {
                            domainAsciiLinks += report
                                    .getDomainAsciiLinkCount();
                            domainLinks += report.getDomainLinkCount();
                            extAsciiLinks += report.getExtAsciiLinkCount();
                            extLinks += report.getExtLinkCounnt();
                            inactiveAsciiLinkCount += report.getInactiveAsciiLinkCount();
                            inactiveLinkCount += report.getInactiveLinkCount();

                            // email info
                            emailFieldCount += report.getEmailFieldCount();
                            emails.addAll(report.getEmails());
                            asciiEmails.addAll(report.getAsciiEmails());

                            if (report.getUrlTOTotalLink() != null) {
                                urlToLinCountkMap.putAll(report
                                        .getUrlTOTotalLink());
                            }
                            *//*if (domainLinks > 100) {
                                break;
                            }*//*

                        }*/
                    }
                }
            } else {
                if (ASCIIFormatChecker.check(websiteUrl)) {
                    extAsciiLinks++;
                }
                extLinks++;
            }
            urlReport = new URLCrawlerReport(domainAsciiLinks, domainAsciiLinksUtf, domainLinks,
                    extAsciiLinks, extLinks, inactiveAsciiLinkCount, inactiveLinkCount,
                    totalLinkInPage, urlToLinCountkMap, asciiEmails, emails, formFields, domainLinksSet);


        }
        return urlReport;
    }


    private void pushData(Set<String> crawledUrls, CrawlingURLInfo crawlingURLInfo) {
        CrawledUrl crawledUrl = new CrawledUrl();
        Set<CrawledUrl> set = new LinkedHashSet<>();
        for (String url : crawledUrls) {
            crawledUrl.setCrawledUrl(url);
            crawledUrl.setCrawlingURLInfo(crawlingURLInfo);
            set.add(crawledUrl);
        }
        crawledUrlRepository.save(set);
    }


}
