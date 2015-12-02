package eu.dzhw.fdz.metadatamanagement.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.CloudServiceConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.Mongo;

import eu.dzhw.fdz.metadatamanagement.config.oauth2.OAuth2AuthenticationReadConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.JSR310DateConverters.DateToLocalDateConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.JSR310DateConverters.DateToLocalDateTimeConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.JSR310DateConverters.LocalDateTimeToDateConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.JSR310DateConverters.LocalDateToDateConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;

/**
 * This Configuration handles the cloud and mongodb instances.
 * 
 * @author Daniel Katzberg
 *
 */
@Configuration
@EnableMongoRepositories("eu.dzhw.fdz.metadatamanagement.repository")
@Profile(Constants.SPRING_PROFILE_CLOUD)
public class CloudMongoDbConfiguration extends AbstractMongoConfiguration {

  private final Logger log = LoggerFactory.getLogger(CloudMongoDbConfiguration.class);

  private MongoDbFactory mongoDbFactory;

  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener() {
    return new ValidatingMongoEventListener(validator());
  }

  @Bean
  public LocalValidatorFactoryBean validator() {
    return new LocalValidatorFactoryBean();
  }

  @Bean
  public Cloud cloud() {
    return new CloudFactory().getCloud();
  }

  @Bean
  @Override
  public MongoDbFactory mongoDbFactory() {

    // create mongodbfactory only once
    if (this.mongoDbFactory == null) {
      this.mongoDbFactory = new CloudServiceConnectionFactory(this.cloud()).mongoDbFactory();
      this.log.debug("Created MongoDBFactory: " + this.mongoDbFactory);
    }

    return this.mongoDbFactory;
  }

  @Bean
  public CustomConversions customConversions() {
    List<Converter<?, ?>> converterList = new ArrayList<>();
    converterList.add(new OAuth2AuthenticationReadConverter());
    converterList.add(DateToZonedDateTimeConverter.INSTANCE);
    converterList.add(ZonedDateTimeToDateConverter.INSTANCE);
    converterList.add(DateToLocalDateConverter.INSTANCE);
    converterList.add(LocalDateToDateConverter.INSTANCE);
    converterList.add(DateToLocalDateTimeConverter.INSTANCE);
    converterList.add(LocalDateTimeToDateConverter.INSTANCE);
    return new CustomConversions(converterList);
  }

  @Override
  protected String getDatabaseName() {
    return this.mongoDbFactory()
      .getDb()
      .getName();
  }

  @Override
  public Mongo mongo() throws DataAccessException, Exception {
    return this.mongoDbFactory()
      .getDb()
      .getMongo();
  }
}
