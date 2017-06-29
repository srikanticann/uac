package com.zensar.uac.web.crawler.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.services.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Created by srikant.singh on 10/06/2016.
 * Purpose of the class: Page class for FaQ
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public class FAQ {


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
                return "UAC-Crawler-HelpGuide-v1.0";
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
