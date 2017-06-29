package com.zensar.uac.web.crawler.config;

import com.zensar.uac.web.crawler.pojo.Weight;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by srikant.singh on 09/28/2016.
 * Purpose of the class: Weightage configuration
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
@Configuration
@PropertySource("classpath:criteria.properties")
public class WeightageConfig {

    @Value("${aggregate.domain.link.weight}")
    private String aggregateDomainLinkWeight;


    @Value("${aggregate.domain.utf8.link.weight}")
    private String aggregateDomainUtfLinkWeight;

    @Value("${aggregate.email.text.weight}")
    private String aggregateEmailTextWeight;

    @Value("${aggregate.email.field.weight}")
    private String aggregateEmailFieldWeight;

    @Value("${aggregate.inactive.link.weight}")
    private String aggregateInactiveLinkWeight;


    @Value("${links_domain.link.weight}")
    private String link_domainLinkWeight;

    @Value("${links_inactive.link.weight}")
    private String link_inactiveLinkWeight;

    @Value("${emails.email.text.weight}")
    private String email_emailTextWeight;

    @Value("${emails.form.field.weight}")
    private String formFieldWeight;


    @Bean
    public Weight weight() {
        Weight weight = new Weight();
        // aggregate compliance weights
        weight.setAggregate_domainLinkWeight(Float.parseFloat(aggregateDomainLinkWeight));
        weight.setAggregate_formFieldWeight(Float.parseFloat(aggregateEmailFieldWeight));
        weight.setAggregate_emailTextWeight(Float.parseFloat(aggregateEmailTextWeight));
        weight.setAggregate_inactiveLinkWeight(Float.parseFloat(aggregateInactiveLinkWeight));

        // link compliance weight
        weight.setLink_domainLinkWeight(Float.parseFloat(link_domainLinkWeight));
        weight.setAggregate_domainUtfLinkWeight(Float.parseFloat(aggregateDomainUtfLinkWeight));
        weight.setLink_inactiveLinkWeight(Float.parseFloat(link_inactiveLinkWeight));

        // email compliance weight
        weight.setEmails_emailTextWeight(Float.parseFloat(email_emailTextWeight));
        weight.setFormFieldWeight(Float.parseFloat(formFieldWeight));

        return weight;
    }
}
