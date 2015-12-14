/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.service;

import java.time.LocalDate;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityExistsException;
import eu.dzhw.fdz.metadatamanagement.service.exception.EntityNotFoundException;

/**
 * @author Daniel Katzberg
 *
 */
public class SurveyServiceTest extends AbstractTest{
  private static final String TEST_PROJECT = "Project1";

  @Inject
  private SurveyRepository surveyRepository;
  
  @Inject
  private FdzProjectRepository fdzProjectRepository;

  @Inject
  private SurveyService surveyService;

  private Survey survey;
  
  private FdzProject fdzProject;

  @Before
  public void beforeTest() {
    this.survey = new SurveyBuilder().withFdzProjectName(TEST_PROJECT)
      .withTitle(new I18nStringBuilder().withDe("titel").withEn("titel").build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now()).withEnd(LocalDate.now()).build())
      .withId("Id")
      .build();
    
    this.fdzProject = new FdzProjectBuilder().withName(TEST_PROJECT).build();
    this.fdzProjectRepository.insert(fdzProject);
  }

  @After
  public void afterEachTest() {
    this.surveyRepository.deleteAll();
    this.fdzProjectRepository.deleteAll();
  }

  @Test(expected = EntityExistsException.class)
  public void testDuplicatedSurvey() {
    // Arrange

    // Act
    this.surveyService.createSurvey(this.survey);
    this.surveyService.createSurvey(this.survey);

    // Assert
  }

  @Test(expected = EntityNotFoundException.class)
  public void testNotUpdateableSurvey() {
    // Arrange

    // Act
    this.surveyService.updateSurvey(this.survey);

    // Assert
  }
}
