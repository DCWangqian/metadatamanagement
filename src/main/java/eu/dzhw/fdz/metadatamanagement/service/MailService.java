package eu.dzhw.fdz.metadatamanagement.service;

import eu.dzhw.fdz.metadatamanagement.config.JHipsterProperties;
import eu.dzhw.fdz.metadatamanagement.domain.User;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

  private final Logger log = LoggerFactory.getLogger(MailService.class);

  @Inject
  private JHipsterProperties jhipsterProperties;

  @Inject
  private JavaMailSenderImpl javaMailSender;

  @Inject
  private MessageSource messageSource;

  @Inject
  private SpringTemplateEngine templateEngine;

  /**
   * System default email address that sends the e-mails.
   */
  @SuppressWarnings("unused")
  private String from;

  /**
   * This method sends a email.
   * @param to the receiver of the email.
   * @param subject the subject of the email.
   * @param content the content of the email.
   * @param isMultipart is the email a multipart mail. 
   * @param isHtml is the email a html mail.
   */
  @Async
  public void sendEmail(String to, String subject, String content, boolean isMultipart,
      boolean isHtml) {
    log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
        isMultipart, isHtml, to, subject, content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message =
          new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
      message.setTo(to);
      message.setFrom(jhipsterProperties.getMail().getFrom());
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);
      log.debug("Sent e-mail to User '{}'", to);
    } catch (Exception e) {
      log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
    }
  }

  /**
   * This method sends an activation mail.
   * @param user the user is the receiver.
   * @param baseUrl the base url of the action url.
   */
  @Async
  public void sendActivationEmail(User user, String baseUrl) {
    log.debug("Sending activation e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("activationEmail", context);
    String subject = messageSource.getMessage("email.activation.title", null, locale);
    sendEmail(user.getEmail(), subject, content, false, true);
  }

  /**
   * This method sends a password reset mail.
   * @param user the user is the receiver.
   * @param baseUrl the base url of the action url.
   */
  @Async
  public void sendPasswordResetMail(User user, String baseUrl) {
    log.debug("Sending password reset e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("passwordResetEmail", context);
    String subject = messageSource.getMessage("email.reset.title", null, locale);
    sendEmail(user.getEmail(), subject, content, false, true);
  }

}
