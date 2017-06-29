package com.zensar.uac.web.crawler.core.database.impl;

import com.zensar.uac.web.crawler.core.database.EntityStoreManager;
import com.zensar.uac.web.crawler.core.database.WebCrawlerData;
import com.zensar.uac.web.crawler.dao.WebCrawlerInfoRepository;
import com.zensar.uac.web.crawler.domain.WebCrawlerReport;
import com.zensar.uac.web.crawler.model.WebCrawlerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sagar on 10/24/2016.
 * Purpose of the class: Implementation for persisting WebCrawlerData
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
@Component
public class EntityStoreManagerImpl implements EntityStoreManager {

    @Autowired
    private WebCrawlerInfoRepository webCrawlerDataRepository;

    public void store(WebCrawlerData webCrawlerData) {
        WebCrawlerReport report = webCrawlerData.getCrawlingReport();

        WebCrawlerInfo webCrawlerInfo = new WebCrawlerInfo(report.getCrawlingURL(), webCrawlerData.getEmailID(), report.getCrawledDate(), report.getDomainAsciiLinkCount(), report.getDomainAsciiUtfLinkCount(),
                report.getDomainLinkCount(), report.getExtAsciiLinkCount(), report.getExtLinkCount(), report.getInactiveAsciiLinkCount(), report.getInactiveLinkCount(), (short) report.getAggregateUAComplianceIndex()
                , report.getAsciiEmailTextCount(), report.getEmailCount(), report.getFormFieldsCount(), report.getProcessingTime(), getUrlDataString(report.getDomainsLinkSet()));

        webCrawlerDataRepository.save(webCrawlerInfo);
    }

    public WebCrawlerData retrieve(String websiteUrl) {
        WebCrawlerInfo webCrawlerInfo = webCrawlerDataRepository.findUacIndexByWebsiteUrl(websiteUrl);
        if (webCrawlerInfo == null) {
            return null;
        }
        WebCrawlerReport webCrawlerReport = new WebCrawlerReport(webCrawlerInfo.getWebsiteUrl(),
                webCrawlerInfo.getNumberOfDomainAsciiLink(), webCrawlerInfo.getNumberOfDomainLink(), webCrawlerInfo.getNumberOfDomainAsciiUtfLink(),
                webCrawlerInfo.getNumberOfExtAsciiLink(), webCrawlerInfo.getNumberOfExtLink(),
                webCrawlerInfo.getInactiveAsciiLinkCount(), webCrawlerInfo.getInactiveLinkCount(),
                webCrawlerInfo.getAsciiEmailCount(), webCrawlerInfo.getEmailCount(),
                webCrawlerInfo.getEmailFieldsCount(), getUrlDataSet(webCrawlerInfo.getCrawledUrls()));

        WebCrawlerData webCrawlerData = new WebCrawlerData(webCrawlerInfo.getRequesterEmail(), webCrawlerReport);

        return webCrawlerData;
    }

    public List<WebCrawlerData> retrieveAllEntity() {
        return null;
    }

    private String getUrlDataString(Set<String> urlLinks) {
        StringBuffer dataString = new StringBuffer();
        for (String urlLink : urlLinks) {
            dataString.append('~');
            dataString.append(urlLink);
        }
        return dataString.length() == 0 ? null : dataString.toString();
    }

    private Set<String> getUrlDataSet(String urlString) {
        String[] arrayURL = urlString.split("~");
        Set<String> urlSet = new LinkedHashSet<String>(Arrays.asList(arrayURL));
        return urlSet;

    }

}
