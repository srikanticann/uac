package com.zensar.uac.web.crawler.services.reports.strategy.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.zensar.uac.web.crawler.services.reports.strategy.CrawlerReportStrategy;
import com.zensar.uac.web.crawler.services.reports.strategy.impl.AllURLStrategy;
import com.zensar.uac.web.crawler.services.reports.strategy.impl.SpecificURLStrategy;
import com.zensar.uac.web.crawler.services.reports.strategy.impl.TopComplexityCountStrategy;

/**
 * Created by Sagar Balai on 10/16/2016.
 * Purpose of the class: Factory class for generating strategy as per request, below
 * are strategy keys are referred and used in strategy generation
 * <p>
 * KEY STRATEGY S SpecificURLStrategy C TopComplexityCountStrategy
 * <p>
 * Specific URL strategy is returned only if strategy key is 'S' and
 * there is not null inputURL value. Default strategy is
 * 'AllURLStrategy' which returns reports for all URL which are present
 * in database.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
@Component
public class CrawlerReportStrategyFactory {

    @Autowired
    @Qualifier("allURLStrategy")
    @Lazy
    private CrawlerReportStrategy allURLStrategy;

    @Autowired
    @Qualifier("specificURLStrategy")
    @Lazy
    private CrawlerReportStrategy specificURLStrategy;

    @Autowired
    @Qualifier("topComplexityCountStrategy")
    @Lazy
    private CrawlerReportStrategy topComplexityCountStrategy;

    public CrawlerReportStrategy getStrategy(String inputURL, String strategyKey) {

        CrawlerReportStrategy strategy = null;

        if (strategyKey.equals("S") && inputURL != null) {
            strategy = specificURLStrategy;
            ((SpecificURLStrategy) specificURLStrategy).setInputURL(inputURL);
        } else if (strategyKey.equals("C")) {
            strategy = topComplexityCountStrategy;
        } else {
            strategy = allURLStrategy;
        }

        return strategy;
    }
}
