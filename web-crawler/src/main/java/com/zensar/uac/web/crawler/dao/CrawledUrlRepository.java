package com.zensar.uac.web.crawler.dao;

import com.zensar.uac.web.crawler.model.CrawledUrl;
import com.zensar.uac.web.crawler.model.CrawlingURLInfo;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by srikant.singh on 10/24/2016.
 * Purpose of the class: This class deals with persisting Crawled internal URLs.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface CrawledUrlRepository extends CrudRepository<CrawledUrl, Long> {

/*    @Transactional
    CrawledUrl save(CrawledUrl crawledUrl);*/

    @Transactional
    List<CrawledUrl> findByCrawledUrl(String crawledUrl);

    @Transactional
    List<CrawledUrl> findBycrawlingURLInfo(CrawlingURLInfo crawlingURLInfo);

}
