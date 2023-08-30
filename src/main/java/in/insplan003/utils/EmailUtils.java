package in.insplan003.utils;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {
	private Logger logger = LoggerFactory.getLogger(EmailUtils.class);
	
	@Autowired
	private JavaMailSender mailSender;
	

	public boolean sendMail(String to, String subject, String body) {
		
		boolean isMailSent = false;
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(mimeMessage); 
			isMailSent =true;
		}catch (Exception e) {
			logger.error("Exception occured",e);
		}
		return isMailSent;
		
	}
}
