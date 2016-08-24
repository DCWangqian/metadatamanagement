package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

/**
 * Test the REST API for {@link DataSetDeleteResource}.
 *
 */
public class DataSetDeleteResourceTest extends AbstractTest {
  
  private static final String API_DATA_SETS_URI = "/api/data-sets";
  private static final String API_DATA_SETS_DELETE_URI = "/api/data-sets/delete";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;
  
  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    rdcProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    dataSetRepository.deleteAll();
  }


  @Test
  public void testDeletingProjectDeletesDataSets() throws Exception {
    
    // create the DataAcquisitionProject
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    rdcProjectRepository.save(project);
    
    // create the Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    
    List<String> variablesId = new ArrayList<>();
    variablesId.add("testID");
    
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(survey.getId());
    
    // create the dataSet
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    dataSetRepository.save(dataSet);
    
    // check that the survey is present
    mockMvc.perform(get(API_DATA_SETS_URI + "/" + dataSet.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(dataSet.getId())))
      .andExpect(jsonPath("$.version", is(0)));

    // delete the Survey
     mockMvc.perform( post(API_DATA_SETS_DELETE_URI+"?dataAcquisitionProjectId="+project.getId())
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());

    // check that the survey has been deleted
     mockMvc.perform(get(API_DATA_SETS_URI + "/" + dataSet.getId() + "?projection=complete"))
     .andExpect(status().isNotFound());
  }
}
