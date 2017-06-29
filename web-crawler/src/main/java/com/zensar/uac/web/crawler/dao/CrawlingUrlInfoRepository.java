package com.zensar.uac.web.crawler.dao;

import com.zensar.uac.web.crawler.model.CrawlingURLInfo;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by srikant.singh on 10/20/2016.
 * Purpose of the class: This class deals with persisting Crawling URL info.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface CrawlingUrlInfoRepository extends CrudRepository<CrawlingURLInfo, Long> {

    @Transactional
    Long deleteByCrawlingUrl(String crawlingUrl);

    @Transactional
    CrawlingURLInfo save(CrawlingURLInfo crawlingURLInfo);

    @Transactional
    int countByCrawlingUrl(String crawlingUrl);

    @Transactional
    CrawlingURLInfo findByCrawlingUrl(String crawlingUrl);

    @Transactional
    int countByCrawlingUrlAndStatus(String crawlingUrl, CrawlingURLInfo.Status status);


}
