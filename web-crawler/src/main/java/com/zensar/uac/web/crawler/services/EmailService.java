package com.zensar.uac.web.crawler.services;

import com.zensar.uac.web.crawler.exception.MessageBuilderException;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by srikant.singh on 10/12/2016.
 * Purpose of the class: ReportService is responsible for Sending emails
 * This is basic interface
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
public interface EmailService {

    EmailService addRecipients(Message.RecipientType var1, String[] var2) throws MessageBuilderException;

    EmailService setSubject(String var1) throws MessageBuilderException;

    EmailService addContent(String var1) throws MessageBuilderException;

    EmailService addAttachment(File var1) throws MessageBuilderException;

    EmailService addAttachmentInline(File var1) throws MessageBuilderException;

    MimeMessage asEmailMessage() throws MessageBuilderException;

    void sendEmail(MimeMessage var1) throws MessageBuilderException;

    EmailService addHtmlContent(String var1) throws MessageBuilderException;
}
