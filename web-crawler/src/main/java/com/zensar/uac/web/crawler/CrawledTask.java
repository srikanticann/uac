package com.zensar.uac.web.crawler;

import com.zensar.uac.web.crawler.complexity.calculation.strategy.factory.ComplexityCalStrategyFactory;
import com.zensar.uac.web.crawler.core.database.EntityStoreManager;
import com.zensar.uac.web.crawler.core.database.WebCrawlerData;
import com.zensar.uac.web.crawler.dao.CrawledUrlRepository;
import com.zensar.uac.web.crawler.dao.CrawlingUrlInfoRepository;
import com.zensar.uac.web.crawler.domain.WebCrawlerReport;
import com.zensar.uac.web.crawler.html.HTMLCrawler;
import com.zensar.uac.web.crawler.model.CrawledUrl;
import com.zensar.uac.web.crawler.model.CrawlingURLInfo;
import com.zensar.uac.web.crawler.services.impl.EmailServiceImpl;
import com.zensar.uac.web.crawler.util.PDFGenerator;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by srikant.singh on 10/16/2016.
 * Purpose of the class: Task for every request for crawling and generate report.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class CrawledTask implements Runnable {

    public CrawledTask(String inputUrl, HTMLCrawler htmlCrawler, /*Set<String> urls,*/ String requestorEmailId, EntityStoreManager entityStoreManager, ComplexityCalStrategyFactory calFactory, CrawlingUrlInfoRepository crawlingUrlInfoRepository, CrawledUrlRepository crawledUrlRepository) {
        this.inputUrl = inputUrl;
        this.htmlCrawler = htmlCrawler;
        this.requestorEmailId = requestorEmailId;
        /*this.urls = urls;*/
        this.entityStoreManager = entityStoreManager;
        this.calFactory = calFactory;
        this.crawlingUrlInfoRepository = crawlingUrlInfoRepository;
        this.crawledUrlRepository = crawledUrlRepository;

    }

    private String inputUrl;

    private HTMLCrawler htmlCrawler;

    private String requestorEmailId;

    private EntityStoreManager entityStoreManager;

    private ComplexityCalStrategyFactory calFactory;

    private CrawlingUrlInfoRepository crawlingUrlInfoRepository;

    private CrawledUrlRepository crawledUrlRepository;

    public void run() {

        CrawlingURLInfo crawlingURLInfo = crawlingUrlInfoRepository.findByCrawlingUrl(inputUrl);
        new CrawlingURLInfo(inputUrl, CrawlingURLInfo.Status.processing, LocalDate.now());
        crawlingUrlInfoRepository.save(crawlingURLInfo);

        crawlingURLInfo.setStatus(CrawlingURLInfo.Status.processing);
        crawlingUrlInfoRepository.save(crawlingURLInfo);
        WebCrawlerReport webCrawlerReport = htmlCrawler.crawl(inputUrl);
        reportWithUrls(webCrawlerReport);
        webCrawlerReport.calculateComplexity(calFactory.getBracketStrategy());
        final String fail = "fail";
        final String failed = "Failed";
        String domainLinkComplianceStatus = failed;
        String inactiveLinkComplianceStatus = failed;
        String emailTextComplianceStatus = failed;
        String emailFieldComplianceStatus = failed;
        String domainLinkComplianceClass = fail;
        String emailTextComplianceClass = fail;
        String emailFieldComplianceClass = fail;
        String inactiveLinkComplianceClass = fail;

        Map<String, String> properties = new HashMap();
        final String pass = "pass";
        final String passed = "Passed";
        if (webCrawlerReport.getDomainLinkCompliance() == 10) {
            domainLinkComplianceStatus = passed;
            domainLinkComplianceClass = pass;
        }
        if (webCrawlerReport.getInactiveLinkCompliance() == 10) {
            inactiveLinkComplianceStatus = passed;
            inactiveLinkComplianceClass = pass;
        }
        if (webCrawlerReport.getEmailTextCompliance() == 10) {
            emailTextComplianceStatus = passed;
            emailTextComplianceClass = pass;
        }
        if (webCrawlerReport.getFormElementsCompliance() == 10) {
            emailFieldComplianceStatus = passed;
            emailFieldComplianceClass = pass;
        }
        properties.put("domainLinkComplianceStatus", domainLinkComplianceStatus);
        properties.put("domainLinkComplianceClass", domainLinkComplianceClass);
        properties.put("inactiveLinkComplianceStatus", inactiveLinkComplianceStatus);
        properties.put("inactiveLinkComplianceClass", inactiveLinkComplianceClass);
        properties.put("emailTextComplianceStatus", emailTextComplianceStatus);
        properties.put("emailTextComplianceClass", emailTextComplianceClass);
        properties.put("emailFieldComplianceStatus", emailFieldComplianceStatus);
        properties.put("emailFieldComplianceClass", emailFieldComplianceClass);
        properties.put("UrlLinks", getUrlDataString(webCrawlerReport.getDomainsLinkSet()));

        String[] To = {webCrawlerReport.getEmailID()};
        try {
            webCrawlerReport.setEmailID(requestorEmailId);
            PDFGenerator.generate(webCrawlerReport, properties);
            String fileName = webCrawlerReport.getCrawlingURL();

            final String http = "http://";
            final String https = "https://";
            final String slash = "/";
            final String blank = "";
            if (fileName.contains(https)) {
                fileName = fileName.replace(https, blank);
            } else if (fileName.contains(http)) {
                fileName = fileName.replace(http, blank);
            }
            if (fileName.contains(slash)) {
                fileName = fileName.replace(slash, blank);
            }
            FileSystem fileSystems = FileSystems.getDefault();
            Path filePath = fileSystems.getPath("PDF/" + fileName.replace(".", "-").replace(":", "-") + ".pdf").normalize();
            File file = filePath.toFile();
            /*MimeMessage message = emailService.addRecipients(Message.RecipientType.TO, To).setSubject("Crawler Report").addContent("Dear User, \n Please find the attached report").addAttachment(file).asEmailMessage();
            emailService.sendEmail(message);*/
            Set<String> dummySet = new LinkedHashSet<>();
            dummySet.add(webCrawlerReport.getCrawlingURL());
            webCrawlerReport.setDomainsLinkSet(dummySet);
            saveInDatabase(requestorEmailId, webCrawlerReport);
            EmailServiceImpl.sendEmail(file, requestorEmailId, "Hi Please find the attached web Cralwer analyser report", "Web Cralwer Analyser Report");

        } catch (Exception me) {
            me.printStackTrace();
        } finally {
            crawlingURLInfo.setStatus(CrawlingURLInfo.Status.completed);
            crawlingUrlInfoRepository.save(crawlingURLInfo);
        }


    }

    private String getUrlDataString(Set<String> urlLinks) {
        StringBuffer dataString = new StringBuffer();
        for (String urlLink : urlLinks) {
            dataString.append('\n');
            dataString.append(urlLink);
        }
        return dataString.length() == 0 ? null : dataString.toString();
    }

    private void saveInDatabase(String emailID, WebCrawlerReport report) {
        WebCrawlerData data = new WebCrawlerData(emailID, report);
        entityStoreManager.store(data);
    }

    private void reportWithUrls(WebCrawlerReport webCrawlerReport) {
        List<CrawledUrl> crawledUrls = crawledUrlRepository.findBycrawlingURLInfo(crawlingUrlInfoRepository.findByCrawlingUrl(webCrawlerReport.getCrawlingURL()));
        Set<String> urls = new LinkedHashSet<>();
        for (CrawledUrl crawledUrl : crawledUrls) {
            urls.add(crawledUrl.getCrawledUrl());
        }
        webCrawlerReport.setDomainsLinkSet(urls);
    }
}
