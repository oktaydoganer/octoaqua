package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.dto.MailNotification;
import com.nexmind3.octoaqua.dto.MailTemplateNotification;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

@Service
public class  EmailManagerImpl implements EmailManager {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Autowired
	private SpringTemplateEngine springTemplateEngine;

	@Override
	public void sendBasicMail(String email, String subject, String body) throws IOException, MessagingException, jakarta.mail.MessagingException {
		final String text = body;

		MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
		helper.setTo(email);
		helper.setSubject(subject);
		helper.setText(text);

		mailSender.send(helper.getMimeMessage());

		log.info("Email notification has been send to {}", email);
	}

	@Override
	public void sendBasicMail(MailNotification mailNotification) throws IOException, MessagingException, jakarta.mail.MessagingException {
		sendBasicMail(mailNotification.getEmail(), mailNotification.getMessage(), mailNotification.getMessage());
	}

	@Override
	public void sendEmailWithTemplate(String templateName, String mailTo, Map<String, Object> variables, String subject) throws IOException, MessagingException {
		log.info("Sending email...");
			try {
				Context context = new Context();
				context.setVariables(variables);
				String process = springTemplateEngine.process(templateName, context);
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setSubject(subject);
				helper.setText(process, true);
				helper.setTo(mailTo);
				//helper.setFrom(username);
				mailSender.send(mimeMessage);
			} catch (MailException | jakarta.mail.MessagingException ex) {
				log.error("Exception occured while sending email to: {}, due to: {}", mailTo, ex.getMessage());
			}
	}

	@Override
	public void sendEmailWithTemplate(MailTemplateNotification mailTemplateNotification) throws IOException, MessagingException {
		sendEmailWithTemplate(mailTemplateNotification.getTemplateName(), mailTemplateNotification.getEmail(), mailTemplateNotification.getVariables(), mailTemplateNotification.getSubject());
	}
}
