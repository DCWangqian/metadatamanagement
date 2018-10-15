package eu.dzhw.fdz.metadatamanagement.mailmanagement.config;

import org.apache.commons.lang.CharEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Configure Thymeleaf for HTML mails.
 */
@Configuration
public class ThymeleafConfiguration {

  /**
   * Configure the template resolver for thymeleaf.
   */
  @Bean
  @Description("Thymeleaf template resolver serving HTML 5 emails")
  public ITemplateResolver emailTemplateResolver() {
    SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
    emailTemplateResolver.setPrefix("classpath:/mails/");
    emailTemplateResolver.setSuffix(".html");
    emailTemplateResolver.setTemplateMode("HTML");
    emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
    emailTemplateResolver.setOrder(1);
    return emailTemplateResolver;
  }
}
