package com.zensar.uac.web.crawler.pages;

import com.zensar.uac.web.crawler.constants.CrawlerConstants;
import com.zensar.uac.web.crawler.domain.WebCrawlerReport;
import com.zensar.uac.web.crawler.exception.URLFetchingException;
import com.zensar.uac.web.crawler.services.ReportService;
import com.zensar.uac.web.crawler.util.ASCIIFormatChecker;
import com.zensar.uac.web.crawler.util.UrlUtil;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by srikant.singh on 10/06/2016.
 * Purpose of the class: Page class for Home
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class Home {

    @Property
    private String URL;

    @Property
    private String emailAddress;

    @Inject
    private ReportService reportService;

    @SessionState(create = false)
    private WebCrawlerReport report;

    @Inject
    private AlertManager alertManager;

    /*@Inject
    @Path("context:images/email_icon.png")
    @Property
    private Asset emailImage;*/

    @OnEvent(EventConstants.ACTIVATE)
    Object onActivate() {
        return true;
    }

    @OnEvent(value = EventConstants.VALIDATE, component = "inputForm")
    void onValidateInputFormForm() {
    }

    @OnEvent(component = "inputForm", value = EventConstants.SUCCESS)
    Object onSuccess() {
        Document doc = null;
        Connection.Response response = null;
        URL = URL.trim();

        if (URL.contains("xn--")) {
            URL = IDN.toUnicode(URL);
        }
        if (!ASCIIFormatChecker.check(URL)) {
            URL = IDN.toASCII(URL);
        }
        String url = UrlUtil.getCorrectHttpUrl(URL);

        try {
            doc = Jsoup.connect(url).get();

        } catch (IOException e) {
            url = CrawlerConstants.HTTPS + CrawlerConstants.COLON
                    + CrawlerConstants.DOUBLE_BACKSLASH_CHARACTER + URL;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e1) {
                try {
                    response = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                            .timeout(10000)
                            .execute();
                    doc = response.parse();
                } catch (IOException e2) {

                    alertManager.error("Your site could not be reached by us, Please contact us for more details.");
                    return Home.class;
                /*throw new URLFetchingException(
                        "There is issue in fetching give input URL :'"
                                + URL
                                + "'. Please try with active valid URL.", e1);*/
                }
                /*throw new URLFetchingException(
                        "There is issue in fetching give input URL :'"
                                + URL
                                + "'. Please try with active valid URL.", e1);*/
            }
        }
        try {
            String[] array = doc.location().split("/");
            String inputUrl = null;
            String uniCodeUrl = URL;

            if (array.length > 3) {
                inputUrl = array[0] + "/" + array[1] + "/" + array[2] + "/";
            } else {
                inputUrl = doc.location();
            }
            int count = reportService.checkCrawlingUrl(inputUrl);
            if (count == 0) {
                if (inputUrl.contains("xn--")) {
                    report = reportService.generateReport(uniCodeUrl, emailAddress);
                } else {
                    report = reportService.generateReport(inputUrl, emailAddress);
                }
            } else {
                alertManager.success("Given URL is under crawling");
                return Home.class;
            }
        } catch (Exception e) {
            return Home.class;
        }
        return Report.class;
    }

}
