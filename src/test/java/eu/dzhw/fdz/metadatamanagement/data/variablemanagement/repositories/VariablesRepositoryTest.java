/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplicationSmokeTest;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * @author Daniel Katzberg
 *
 */
public class VariablesRepositoryTest extends MetaDataManagementApplicationSmokeTest {

  @Autowired
  private VariableRepository variablesRepository;

  @Test
  public void testMatchQueryInAllField() {
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    assertThat(this.variablesRepository.matchQueryInAllField("A name", new PageRequest(0, 10))
        .getNumberOfElements(), is(10));
  }

  @Test
  public void testMatchPhrasePrefixQuery() {

    LocaleContextHolder.setLocale(Locale.GERMAN);
    Page<VariableDocument> result =
        this.variablesRepository.matchPhrasePrefixQuery("FdZ_ID01", new PageRequest(0, 20));
    assertThat(result.getNumberOfElements(), is(15));
  }
  
  @Test
  public void testMatchFilterBySurveyId() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
    Page<VariableDocument> result =
        this.variablesRepository.matchFilterBySurveyId("Ge_ALL_04", "Staatsbürgerschaft");
    assertThat(result.getNumberOfElements(), is(1));
  }

}
