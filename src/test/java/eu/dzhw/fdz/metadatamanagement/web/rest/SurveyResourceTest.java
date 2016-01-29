package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;

/**
 * Test the REST API for {@link Survey}s.
 * 
 * @author René Reitmann
 */
public class SurveyResourceTest extends AbstractTest {
  private static final String API_SURVEYS_URI = "/api/surveys";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private FdzProjectRepository fdzProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    fdzProjectRepository.deleteAll();
    surveyRepository.deleteAll();
  }

  @Test
  public void testCreateValidSurvey() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId")
      .withCufDoi("testDoi")
      .withSufDoi("testDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testId")
      .withFdzProject(project)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id
    mockMvc
      .perform(put(API_SURVEYS_URI + "/" + survey.getId()).content(convertSurveyToJson(survey)))
      .andExpect(status().isCreated());

    // read the survey under the new url
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isOk());
  }

  @Test
  public void testCreateSurveyWithInvalidPeriod() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId")
      .withCufDoi("testDoi")
      .withSufDoi("testDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testId")
      .withFdzProject(project)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .minusDays(1))
        .build())
      .build();

    // create the survey with the given id but with wrong period
    MvcResult result = mockMvc
      .perform(put(API_SURVEYS_URI + "/" + survey.getId()).content(convertSurveyToJson(survey)))
      .andExpect(status().isBadRequest())
      .andReturn();
  }

  @Test
  public void testCreateSurveyWithInvalidProject() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId")
      .withCufDoi("testDoi")
      .withSufDoi("testDoi")
      .build();

    Survey survey = new SurveyBuilder().withId("testId")
      .withFdzProject(project)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id but with an unknown project
    mockMvc
      .perform(put(API_SURVEYS_URI + "/" + survey.getId()).content(convertSurveyToJson(survey)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteSurvey() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId")
      .withCufDoi("testDoi")
      .withSufDoi("testDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testId")
      .withFdzProject(project)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id
    mockMvc
      .perform(put(API_SURVEYS_URI + "/" + survey.getId()).content(convertSurveyToJson(survey)))
      .andExpect(status().isCreated());

    // delete the survey
    mockMvc.perform(delete(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the survey is really deleted
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateSurvey() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId")
      .withCufDoi("testDoi")
      .withSufDoi("testDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testId")
      .withFdzProject(project)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();

    // create the survey with the given id
    mockMvc
      .perform(put(API_SURVEYS_URI + "/" + survey.getId()).content(convertSurveyToJson(survey)))
      .andExpect(status().isCreated());

    // update the survey
    survey.getTitle()
      .setDe("titel2");
    mockMvc
      .perform(put(API_SURVEYS_URI + "/" + survey.getId()).content(convertSurveyToJson(survey)))
      .andExpect(status().isNoContent());

    // get the survey and check the updated title and version
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.title.de", is("titel2")));
  }
  
  @Test
  public void testDeletingProjectDeletesSurvey() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testId")
      .withCufDoi("testDoi")
      .withSufDoi("testDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testId")
      .withFdzProject(project)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now()
          .plusDays(1))
        .build())
      .build();
    
    surveyRepository.save(survey);
    
    // check that the survey is present
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(project.getId())))
      .andExpect(jsonPath("$.version", is(0)));
    
    // delete the project
    mockMvc.perform(delete("/api/fdz_projects/" + project.getId())).andExpect(status().is2xxSuccessful());
    
    // check that the survey has been deleted as well
    mockMvc.perform(get(API_SURVEYS_URI + "/" + survey.getId() + "?projection=complete"))
      .andExpect(status().isNotFound());
    
  }

  private String convertSurveyToJson(Survey survey) throws JsonSyntaxException, IOException {
    JsonObject jsonObject =
        new JsonParser().parse(new String(TestUtil.convertObjectToJsonBytes(survey)))
          .getAsJsonObject();

    // we need to adjust the referenced project
    FdzProject project = survey.getFdzProject();
    if (project != null) {
      jsonObject.remove("fdzProject");
      jsonObject.addProperty("fdzProject", "/api/fdz_projects/" + project.getId());
    }

    return jsonObject.toString();
  }
}
