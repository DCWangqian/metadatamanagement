package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import io.swagger.annotations.Api;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Enable swagger for all API endpoints.
 */
@Configuration
@EnableSwagger2
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfiguration {

  /**
   * Expose only the docs for the order API.
   */
  @Bean
  public Docket api(Environment env) {
    if (env.acceptsProfiles(Constants.SPRING_PROFILE_LOCAL)) {
      return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
      .apis(RequestHandlerSelectors.any())
      .paths(PathSelectors.any()).build();
    }
    return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).select()
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.any()).build();
  }
}
