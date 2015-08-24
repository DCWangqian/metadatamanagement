/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableSurveyTest {

  @Test
  public void testHashCode() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("Survey01").build();

    // Act

    // Assert
    assertThat(variableSurvey.hashCode(), not(0));
  }

  @Test
  public void testEquals() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().build();
    VariableSurvey variableSurvey2 = new VariableSurveyBuilder().build();

    DateRange dateRange = new DateRange();
    dateRange.setStartDate(LocalDate.now().plusDays(1));
    DateRange dateRange2 = new DateRange();
    dateRange2.setStartDate(LocalDate.now().plusDays(2));

    // Act
    boolean checkNullObject = variableSurvey.equals(null);
    boolean checkSame = variableSurvey.equals(variableSurvey);
    boolean checkDifferentClass = variableSurvey.equals(new Object());
    variableSurvey2.setSurveyId("ID2");
    boolean checkSurveyOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyId("ID2");
    variableSurvey2.setSurveyPeriod(dateRange2);
    boolean checkSurveyPeriodOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setSurveyPeriod(dateRange2);
    variableSurvey2.setTitle("Title 2");
    boolean checkTitleOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setTitle("Title 2");
    variableSurvey2.setVariableAlias("Alias 2");
    boolean checkVariableAliasOther = variableSurvey.equals(variableSurvey2);
    variableSurvey.setVariableAlias("Alias 2");
    boolean checkVariableAliasBothSame = variableSurvey.equals(variableSurvey2);

    // Assert
    assertEquals(false, checkNullObject);
    assertEquals(true, checkSame);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkSurveyOther);
    assertEquals(false, checkSurveyPeriodOther);
    assertEquals(false, checkTitleOther);
    assertEquals(false, checkVariableAliasOther);
    assertEquals(true, checkVariableAliasBothSame);
  }

}
