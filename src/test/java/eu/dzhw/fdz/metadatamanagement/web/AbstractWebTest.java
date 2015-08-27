package eu.dzhw.fdz.metadatamanagement.web;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplication;
import eu.dzhw.fdz.metadatamanagement.config.i18n.I18nConfiguration;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;

/**
 * Base class for all MVC Controller tests. Sets up the application context and initializes the mvc
 * mock and enables the test profile.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MetaDataManagementApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public abstract class AbstractWebTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private VariableRepository variableRepository;

  protected MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @PostConstruct
  protected void cleanDatabase() {
    Locale currentLocale = LocaleContextHolder.getLocale();
    for (Locale locale : I18nConfiguration.SUPPORTED_LANGUAGES) {
      LocaleContextHolder.setLocale(locale);
      variableRepository.deleteAll();
    }
    LocaleContextHolder.setLocale(currentLocale);
  }
}
