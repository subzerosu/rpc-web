package cane.brothers.rpc.service.email;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import cane.brothers.rpc.config.RpcMailProperties;

/**
 * Send e-mail via spring.
 */
@Component
public class EmailManagerService implements EmailManager {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MailProperties mailProps;

	@Autowired
	private RpcMailProperties rpcProps;

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * Constructor
	 */
	public EmailManagerService() {
		logger.debug("create email manager");
	}

	@Override
	public boolean sendEmail(String output) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {

				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(rpcProps.getTo()));
				if (rpcProps.getCc() != null) {
					mimeMessage.setRecipient(Message.RecipientType.CC, new InternetAddress(rpcProps.getCc()));
				}

				mimeMessage.setFrom(new InternetAddress(mailProps.getUsername()));
				mimeMessage.setSubject(rpcProps.getSubject());

				// TODO content, template
				mimeMessage.setText("hello");
			}
		};

		try {
			logger.info("Отправляем письмо на {}.", rpcProps.getTo());
			this.mailSender.send(preparator);

			logger.info("Сообщение было отправленно успешно.");
		} catch (MailException ex) {
			logger.error("Проблемы при отправки сообщения.");
			logger.error(ex.getMessage());
			return false;
		}

		return true;
	}
}
