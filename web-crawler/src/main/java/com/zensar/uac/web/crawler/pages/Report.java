package com.zensar.uac.web.crawler.pages;

import com.zensar.uac.web.crawler.domain.WebCrawlerReport;
import com.zensar.uac.web.crawler.services.EmailService;
import com.zensar.uac.web.crawler.services.impl.EmailServiceImpl;
import com.zensar.uac.web.crawler.util.PDFGenerator;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by srikant.singh on 10/06/2016.
 * Purpose of the class: Page class for Report
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class Report {

    private final String fail = "fail";
    private final String failed = "Failed";

    @SessionState(create = false)
    private WebCrawlerReport report;

    @Property
    @Persist
    private int uAComplianceIndex;

    @Property
    private int domainLinkCompliance;

    @Property
    @SessionState(create = false)
    private String crawlingURL;

    @Property
    private int emailTextCompliance;

    @Property
    private int emailFieldCompliance;

    @Property
    private int htmlUnicodeCompliance;

    @Property
    private int inactiveLinkCompliance;

    @Property
    private int linkComplianceIndex;

    @Property
    private int emailComplianceIndex;

    @Property
    private int formElementsCompliance;

    @Property
    private String domainLinkComplianceStatus = failed;

    @Property
    private String inactiveLinkComplianceStatus = failed;

    @Property
    private String emailTextComplianceStatus = failed;

    @Property
    private String emailFieldComplianceStatus = failed;

    @Property
    private String domainLinkComplianceClass = fail;

    @Property
    private String emailTextComplianceClass = fail;

    @Property
    private String emailFieldComplianceClass = fail;

    @Property
    private String inactiveLinkComplianceClass = fail;

    @Property
    private String viewReportData;

    @Property
    @SessionState(create = false)
    private String requestEmail;

    /*
    @Inject
    private EmailService emailService;
    */

    @Inject
    Block ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, On, Tw, Th,
            Fo, Fi, Si, Se, Ei, Ni, Te, O1, T2, T3, F4, F5, S6, S7, E8, N9, T10;

    @OnEvent(EventConstants.ACTIVATE)
    Object onActivate() {
        if (report == null) {
            return Home.class;
        } else if (report.getDomainAsciiLinkCount() == -10) {

            requestEmail = report.getCrawlingURL() + "~" + report.getEmailID();

            return MessagePage.class;
        }
        uAComplianceIndex = report.getAggregateUAComplianceIndex();
        crawlingURL = report.getCrawlingURL();
        domainLinkCompliance = report.getDomainLinkCompliance();
        emailTextCompliance = report.getEmailTextCompliance();
        inactiveLinkCompliance = report.getInactiveLinkCompliance();
        htmlUnicodeCompliance = report.getHtmlUnicodeCompliance();
        linkComplianceIndex = report.getLinkComplianceIndex();
        formElementsCompliance = report.getFormElementsCompliance();
        emailComplianceIndex = report.getEmailComplianceIndex();

        viewReportData = getReportData();
        Map<String, String> properties = new HashMap();
        final String pass = "pass";
        final String passed = "Passed";
        if (domainLinkCompliance == 10) {
            domainLinkComplianceStatus = passed;
            domainLinkComplianceClass = pass;
        }
        if (inactiveLinkCompliance == 10) {
            inactiveLinkComplianceStatus = passed;
            inactiveLinkComplianceClass = pass;
        }
        if (emailTextCompliance == 10) {
            emailTextComplianceStatus = passed;
            emailTextComplianceClass = pass;
        }
        if (emailFieldCompliance == 10) {
            emailFieldComplianceStatus = passed;
            emailFieldComplianceClass = pass;

        }
        properties.put("domainLinkComplianceStatus", domainLinkComplianceStatus);
        properties.put("domainLinkComplianceClass", domainLinkComplianceClass);
        properties.put("inactiveLinkComplianceStatus", inactiveLinkComplianceStatus);
        properties.put("inactiveLinkComplianceClass", inactiveLinkComplianceClass);
        properties.put("emailTextComplianceStatus", emailTextComplianceStatus);
        properties.put("emailTextComplianceClass", emailTextComplianceClass);
        properties.put("emailFieldComplianceStatus", emailFieldComplianceStatus);
        properties.put("emailFieldComplianceClass", emailFieldComplianceClass);
        properties.put("UrlLinks", getUrlDataString(report.getDomainsLinkSet()));
        properties.put("domainLinkComplianceStatus", domainLinkComplianceStatus);

        //String[] To = {report.getEmailID()};
        try {
            PDFGenerator.generate(report, properties);
            String fileName = crawlingURL;

            final String http = "http://";
            final String https = "https://";
            final String slash = "/";
            final String blank = "";
            if (fileName.contains(https)) {
                fileName = fileName.replace(https, blank);
            } else if (fileName.contains(http)) {
                fileName = fileName.replace(http, blank);
            }
            if (fileName.contains(slash)) {
                fileName = fileName.replace(slash, blank);
            }


            FileSystem fileSystems = FileSystems.getDefault();
            Path filePath = fileSystems.getPath("PDF/" + fileName.replace(".", "-").replace(":", "-") + ".pdf").normalize();
            File file = filePath.toFile();
            /*MimeMessage message = emailService.addRecipients(Message.RecipientType.TO, To).setSubject("Crawler Report").addContent("Dear User, \n Please find the attached report").addAttachment(file).asEmailMessage();
            emailService.sendEmail(message);*/
            EmailServiceImpl.sendEmail(file, report.getEmailID(), "Hi Please find the attached web Cralwer analyser report", "Web Cralwer Analyser Report");
        } catch (Exception me) {
            me.printStackTrace();
        }
        return true;
    }

    public Block getCase() {
        if (uAComplianceIndex == 1) {
            return ONE;
        } else if (uAComplianceIndex == 2) {
            return TWO;
        } else if (uAComplianceIndex == 3) {
            return THREE;

        } else if (uAComplianceIndex == 4) {
            return FOUR;
        } else if (uAComplianceIndex == 5) {
            return FIVE;

        } else if (uAComplianceIndex == 6) {
            return SIX;
        } else if (uAComplianceIndex == 7) {
            return SEVEN;

        } else if (uAComplianceIndex == 8) {
            return EIGHT;
        } else if (uAComplianceIndex == 9) {
            return NINE;
        } else if (uAComplianceIndex == 10) {
            return TEN;

        } else {
            return null;
        }
    }

    public Block getInternalsLinks() {
        if (linkComplianceIndex == 1) {
            return On;
        } else if (linkComplianceIndex == 2) {
            return Tw;
        } else if (linkComplianceIndex == 3) {
            return Th;

        } else if (linkComplianceIndex == 4) {
            return Fo;
        } else if (linkComplianceIndex == 5) {
            return Fi;

        } else if (linkComplianceIndex == 6) {
            return Si;
        } else if (linkComplianceIndex == 7) {
            return Se;

        } else if (linkComplianceIndex == 8) {
            return Ei;
        } else if (linkComplianceIndex == 9) {
            return Ni;
        } else if (linkComplianceIndex == 10) {
            return Te;

        } else {
            return null;
        }
    }

    public Block getFormElements() {
        if (formElementsCompliance == 1) {
            return O1;
        } else if (formElementsCompliance == 2) {
            return T2;
        } else if (formElementsCompliance == 3) {
            return T3;

        } else if (formElementsCompliance == 4) {
            return F4;
        } else if (formElementsCompliance == 5) {
            return F5;

        } else if (formElementsCompliance == 6) {
            return S6;
        } else if (formElementsCompliance == 7) {
            return S7;

        } else if (formElementsCompliance == 8) {
            return E8;
        } else if (formElementsCompliance == 9) {
            return N9;
        } else if (formElementsCompliance == 10) {
            return T10;

        } else {
            return null;
        }
    }


    private String getReportData() {
        String s = "URL-" +
                report.getCrawlingURL() + "#Requestor Email-" + report.getEmailID() + "#Number of Domain ASCII Link-"
                + report.getDomainAsciiLinkCount() + "#Number of Domain Link-" + report.getDomainLinkCount() + "#Number of Ext ASCII Link-"
                + report.getExtAsciiLinkCount() + "#Number of Ext Links-" + report.getExtLinkCount() + "#Number of Domain Inactive link-"
                + report.getInactiveLinkCount() + "#Email Counts-" + report.getAsciiEmailTextCount() + "#Email Fields Counts-"
                + report.getFormFieldsCount();
        return s;
    }

    public boolean getHtmlUnicodeCriteria() {
        if (htmlUnicodeCompliance == 10) {
            return true;
        }
        return false;
    }

    public boolean getInactiveLinkCriteria() {
        if (inactiveLinkCompliance == 10) {
            return true;
        }
        return false;
    }

    public boolean getEmailTextCriteria() {
        if (emailTextCompliance == 10) {
            return true;
        }
        return false;
    }

    public boolean getFormElementCriteria() {
        if (formElementsCompliance == 10) {
            return true;
        }
        return false;
    }

    private String getUrlDataString(Set<String> urlLinks) {
        StringBuffer dataString = new StringBuffer();
        for (String urlLink : urlLinks) {
            dataString.append('\n');
            dataString.append(urlLink);
        }
        return dataString.length() == 0 ? null : dataString.toString();
    }

    @OnEvent(component = "exportLink", value = EventConstants.ACTION)
    public StreamResponse onExportClick() {
        return getExport();
    }

    private StreamResponse getExport() {
        return new StreamResponse() {
            @Override
            public void prepareResponse(Response response) {
                setHeaders(response, getFilename());
            }

            private String getFilename() {
                String fileName = crawlingURL;
                final String http = "http://";
                final String https = "https://";
                final String slash = "/";
                final String blank = "";

                if (fileName.contains(https)) {
                    fileName = fileName.replace(https, blank);
                } else if (fileName.contains(http)) {
                    fileName = fileName.replace(http, blank);
                }
                if (fileName.contains(slash)) {
                    fileName = fileName.replace(slash, blank);
                }

                return fileName.replace(".", "-").replace(":", "-");
            }

            @Override
            public InputStream getStream() throws IOException {
                InputStream pdfExport = null;
                FileSystem fileSystems = FileSystems.getDefault();
                Path filePath = fileSystems.getPath("PDF/" + getFilename() + ".pdf").normalize();
                File file = filePath.toFile();
                pdfExport = new FileInputStream(file);

                return pdfExport;
            }

            @Override
            public String getContentType() {
                return "text/pdf";
            }
        };
    }

    public static void setHeaders(Response response, String fileName) {
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + '"' + fileName + ".pdf" + '"');
    }

}
