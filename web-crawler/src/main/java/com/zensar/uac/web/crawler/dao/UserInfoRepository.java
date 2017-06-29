package com.zensar.uac.web.crawler.dao;

import com.zensar.uac.web.crawler.model.UserDetailInfo;
import com.zensar.uac.web.crawler.model.WebCrawlerInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by srikant.singh on 10/16/2016.
 * Purpose of the class: This class deals with persisting User Details info.
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface UserInfoRepository extends CrudRepository<UserDetailInfo, Long> {

    UserDetailInfo save(UserDetailInfo userDetailInfo);

    List<UserDetailInfo> findAll();
}
