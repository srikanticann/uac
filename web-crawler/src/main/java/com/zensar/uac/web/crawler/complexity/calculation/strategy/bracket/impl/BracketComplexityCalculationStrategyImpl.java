package com.zensar.uac.web.crawler.complexity.calculation.strategy.bracket.impl;

import com.zensar.uac.web.crawler.complexity.calculation.strategy.bracket.BracketComplexityCalculationStrategy;
import com.zensar.uac.web.crawler.domain.WebCrawlerReport;
import com.zensar.uac.web.crawler.pojo.Weight;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Sagar Balai on 10/24/2016.
 * Purpose of the class: This class is use for strategy for bracket complexity calculation.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
@Component
public class BracketComplexityCalculationStrategyImpl implements
        BracketComplexityCalculationStrategy {
    @Inject
    private Weight weight;

    @Override
    public int calculateAggregateCompliance(WebCrawlerReport webCrawlerReport) {
        float complexity = 0;

        complexity = calculateDomainLinkCompliance(webCrawlerReport.getDomainAsciiLinkCount(), webCrawlerReport.getDomainLinkCount()) * weight.getAggregate_domainLinkWeight()
                + calculateDomainUtfLinkCompliance(webCrawlerReport.getDomainAsciiLinkCount(), webCrawlerReport.getDomainAsciiUtfLinkCount()) * weight.getAggregate_domainUtfLinkWeight()
                + calculateComplexityFactor(webCrawlerReport.getExtLinkCount()) * weight.getAggregate_inactiveLinkWeight()
                + calculateEmailTextCompliance(webCrawlerReport.getAsciiEmailTextCount(), webCrawlerReport.getEmailCount()) * weight.getAggregate_emailTextWeight()
                + formElementsCompliance(webCrawlerReport.getDomainLinkCount(), webCrawlerReport.getFormFieldsCount()) * weight.getAggregate_formFieldWeight();

        return Math.round(complexity);
    }

    @Override
    public int calculateDomainLinkCompliance(int domainAsciiLinkCount, int domainLinkCount) {
        if (domainAsciiLinkCount == domainLinkCount) {
            if (domainAsciiLinkCount == 0) {
                return 10;
            }
            return 1;
        }
        return 10 - (int) (((float) domainAsciiLinkCount / (float) domainLinkCount) * 10);

    }

    @Override
    public int calculateDomainUtfLinkCompliance(int domainAsciiLinkCount, int domainUtfLinkCount) {
        if (domainAsciiLinkCount == domainUtfLinkCount) {
            return 10;
        }
        return 10 - (int) (((float) domainUtfLinkCount / (float) domainAsciiLinkCount) * 10);

    }

    @Override
    public int calculateEmailTextCompliance(int asciiEmailCount, int emailCount) {
        if (asciiEmailCount == emailCount) {
            if (asciiEmailCount == 0) {
                return 10;
            }
            return 1;
        }
        return 10 - (int) (((float) asciiEmailCount / (float) emailCount) * 10);
    }

    @Override
    public int calculateEmailFieldCompliance(int emailFeildCount) {
        return calculateComplexityFactor(emailFeildCount);
    }

    @Override
    public int calculateInactiveLinkCompliance(int inactiveLinkCount) {
        return calculateComplexityFactor(inactiveLinkCount);
    }


    // link compliance
    @Override
    public int calculateLinkCompliance(WebCrawlerReport webCrawlerReport) {
        float complexity = 0;

        complexity = calculateDomainUtfLinkCompliance(webCrawlerReport.getDomainAsciiLinkCount(), webCrawlerReport.getDomainAsciiUtfLinkCount()) * weight.getAggregate_domainUtfLinkWeight()
                + calculateComplexityFactor(webCrawlerReport.getExtLinkCount()) * weight.getLink_inactiveLinkWeight();
        return Math.round(complexity);
    }

    @Override
    public int calculateEmailCompliance(WebCrawlerReport webCrawlerReport) {
        float complexity = 0;

        complexity = calculateComplexityFactor(webCrawlerReport.getFormFieldsCount()) * weight.getFormFieldWeight()
                + calculateEmailTextCompliance(webCrawlerReport.getAsciiEmailTextCount(), webCrawlerReport.getEmailCount()) * weight.getEmails_emailTextWeight();
        return Math.round(complexity);
    }


    private int calculateComplexityFactor(int entityCount) {
        int complexityFactor = 10 - entityCount;

        if (entityCount >= 10) {
            complexityFactor = 1;
        }
        return complexityFactor;
    }

    @Override
    public int formElementsCompliance(int domainLinkCount, int formFieldsCount) {
        int complianceFactor = 0;
        if (formFieldsCount == 0) {
            complianceFactor = 10;
            return complianceFactor;
        }

        int percentageOfFormFileds = Math.round(((float) formFieldsCount / (float) domainLinkCount)) * 100;
        if (percentageOfFormFileds >= 10) {
            complianceFactor = 1;
        } else if (percentageOfFormFileds == 9) {
            complianceFactor = 2;
        } else if (percentageOfFormFileds == 8) {
            complianceFactor = 3;
        } else if (percentageOfFormFileds == 7) {
            complianceFactor = 4;
        } else if (percentageOfFormFileds == 6) {
            complianceFactor = 5;
        } else if (percentageOfFormFileds == 5) {
            complianceFactor = 6;
        } else if (percentageOfFormFileds == 4) {
            complianceFactor = 7;
        } else if (percentageOfFormFileds == 3) {
            complianceFactor = 8;
        } else if (percentageOfFormFileds == 2) {
            complianceFactor = 9;
        } else if (percentageOfFormFileds == 1) {
            complianceFactor = 10;
        }
        return complianceFactor;
    }
}

