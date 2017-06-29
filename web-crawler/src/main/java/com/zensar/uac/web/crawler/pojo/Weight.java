package com.zensar.uac.web.crawler.pojo;

/**
 * Created by srikant.singh on 09/28/2016.
 */

/**
 * Created by srikant.singh on 09/28/2016.
 * Purpose of the class: POJO for Weight
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class Weight {

    // aggregate compliance weights
    private float aggregate_domainLinkWeight;
    private float aggregate_domainUtfLinkWeight;
    private float aggregate_inactiveLinkWeight;
    private float aggregate_emailTextWeight;
    private float aggregate_formFieldWeight;

    // link compliance weights
    private float link_domainLinkWeight;
    private float link_inactiveLinkWeight;

    // emails compliance weights
    private float emails_emailTextWeight;
    private float formFieldWeight;

    public float getAggregate_domainLinkWeight() {
        return aggregate_domainLinkWeight;
    }

    public void setAggregate_domainLinkWeight(float aggregate_domainLinkWeight) {
        this.aggregate_domainLinkWeight = aggregate_domainLinkWeight;
    }

    public float getAggregate_domainUtfLinkWeight() {
        return aggregate_domainUtfLinkWeight;
    }

    public void setAggregate_domainUtfLinkWeight(float aggregate_domainUtfLinkWeight) {
        this.aggregate_domainUtfLinkWeight = aggregate_domainUtfLinkWeight;
    }

    public float getAggregate_emailTextWeight() {
        return aggregate_emailTextWeight;
    }

    public void setAggregate_emailTextWeight(float aggregate_emailTextWeight) {
        this.aggregate_emailTextWeight = aggregate_emailTextWeight;
    }

    public float getAggregate_formFieldWeight() {
        return aggregate_formFieldWeight;
    }

    public void setAggregate_formFieldWeight(float aggregate_formFieldWeight) {
        this.aggregate_formFieldWeight = aggregate_formFieldWeight;
    }

    public float getAggregate_inactiveLinkWeight() {
        return aggregate_inactiveLinkWeight;
    }

    public void setAggregate_inactiveLinkWeight(float aggregate_inactiveLinkWeight) {
        this.aggregate_inactiveLinkWeight = aggregate_inactiveLinkWeight;
    }

    public float getLink_domainLinkWeight() {
        return link_domainLinkWeight;
    }

    public void setLink_domainLinkWeight(float link_domainLinkWeight) {
        this.link_domainLinkWeight = link_domainLinkWeight;
    }

    public float getLink_inactiveLinkWeight() {
        return link_inactiveLinkWeight;
    }

    public void setLink_inactiveLinkWeight(float link_inactiveLinkWeight) {
        this.link_inactiveLinkWeight = link_inactiveLinkWeight;
    }

    public float getEmails_emailTextWeight() {
        return emails_emailTextWeight;
    }

    public void setEmails_emailTextWeight(float emails_emailTextWeight) {
        this.emails_emailTextWeight = emails_emailTextWeight;
    }

    public float getFormFieldWeight() {
        return formFieldWeight;
    }

    public void setFormFieldWeight(float formFieldWeight) {
        this.formFieldWeight = formFieldWeight;
    }
}
