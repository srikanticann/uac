package com.zensar.uac.web.crawler.complexity.calculation.strategy;

import com.zensar.uac.web.crawler.domain.WebCrawlerReport;

/**
 * Created by Sagar Balai on 10/24/2016.
 * Purpose of the class: Strategy interface to calculate complexity of given site
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface ComplexityCalculationStrategy {

    int calculateAggregateCompliance(WebCrawlerReport webCrawlerReport);

    int calculateEmailTextCompliance(int asciiEmailCount, int emailCount);

    int calculateEmailFieldCompliance(int emailFeildCount);

    int calculateInactiveLinkCompliance(int inactiveLinkCount);

    int calculateDomainLinkCompliance(int domainAsciiLinkCount, int domainLinkCount);

    int calculateDomainUtfLinkCompliance(int domainAsciiLinkCount, int domainUtfLinkCount);

    int calculateLinkCompliance(WebCrawlerReport webCrawlerReport);

    int calculateEmailCompliance(WebCrawlerReport webCrawlerReport);

    int formElementsCompliance(int domainLinkCount, int formFieldsCount);
}
