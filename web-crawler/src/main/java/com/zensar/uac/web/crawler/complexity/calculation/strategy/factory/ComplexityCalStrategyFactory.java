package com.zensar.uac.web.crawler.complexity.calculation.strategy.factory;

import com.zensar.uac.web.crawler.complexity.calculation.strategy.ComplexityCalculationStrategy;
import com.zensar.uac.web.crawler.complexity.calculation.strategy.bracket.BracketComplexityCalculationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Sagar Balai on 10/24/2016.
 * Purpose of the class: Strategy class to calculate complexity of given site
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
@Component
public class ComplexityCalStrategyFactory {
    @Autowired
    private BracketComplexityCalculationStrategy calStrategy;

    public ComplexityCalculationStrategy getBracketStrategy() {
        return calStrategy;
    }

}
