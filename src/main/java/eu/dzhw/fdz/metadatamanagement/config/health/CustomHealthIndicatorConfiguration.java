package eu.dzhw.fdz.metadatamanagement.config.health;

import org.elasticsearch.client.Client;
import org.springframework.boot.actuate.health.ElasticsearchHealthIndicatorProperties;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration which enable the custom elastic search health indicator.
 * 
 * @author René Reitmann
 */
@Configuration
public class CustomHealthIndicatorConfiguration {

  @Bean(name = "elasticsearchHealthIndicator")
  public HealthIndicator customElasticSearchHealthIndicator(Client client,
      ElasticsearchHealthIndicatorProperties properties) {
    return new CustomElasticSearchHealthIndicator(client, properties);
  }
}
