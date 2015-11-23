/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.config.MetadataManagementProperties.ElasticsearchAngularClient;
import eu.dzhw.fdz.metadatamanagement.config.MetadataManagementProperties.ElasticsearchClient;

/**
 * @author Daniel Katzberg
 *
 */
public class MetadataManagementPropertiesTest {
	
	
	@Test
	public void testElasticSearchClient() {
		//Arrange 
		MetadataManagementProperties managementProperties = new MetadataManagementProperties();
		
		//Act
		ElasticsearchClient client = managementProperties.getElasticsearchClient();		
		client.setUrl("http://localhost:1234");
				
		//Assert
		assertThat(client, not(nullValue()));
		assertThat(client.getUrl(), is("http://localhost:1234"));
	}
	
	@Test
	public void testElasticSearchAngularClient() {
		//Arrange 
		MetadataManagementProperties managementProperties = new MetadataManagementProperties();
		
		//Act
		ElasticsearchAngularClient angularClient = managementProperties.getElasticsearchAngularClient();		
		angularClient.setLogLevel("info");
		angularClient.setUrl("http://localhost:1234");
		angularClient.setVersionApi("1.7");
				
		//Assert
		assertThat(angularClient, not(nullValue()));
		assertThat(angularClient.getLogLevel(), is("info"));
		assertThat(angularClient.getVersionApi(), is("1.7"));
		assertThat(angularClient.getUrl(), is("http://localhost:1234"));
	}
}
