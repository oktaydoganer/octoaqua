package com.nexmind3.octoaqua.service;



import com.nexmind3.octoaqua.dto.MailNotification;
import com.nexmind3.octoaqua.dto.MailTemplateNotification;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailManager {

	void sendBasicMail(String email, String subject, String body) throws IOException, MessagingException, jakarta.mail.MessagingException;
	void sendBasicMail(MailNotification mailNotification) throws IOException, MessagingException, jakarta.mail.MessagingException;
	void sendEmailWithTemplate(String templateName, String mailTo, Map<String, Object> variables, String subject) throws IOException, MessagingException;

	void sendEmailWithTemplate(MailTemplateNotification mailTemplateNotification) throws IOException, MessagingException;
	}
