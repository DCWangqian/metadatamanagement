package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonSyntaxException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.web.rest.TestUtil;

/**
 * @author Daniel Katzberg
 *
 */
public class DataSetResourceTest extends AbstractTest {
  private static final String API_DATASETS_URI = "/api/data_sets";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

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
    this.dataAcquisitionProjectRepository.deleteAll();
    this.surveyRepository.deleteAll();
    this.dataSetRepository.deleteAll();
  }


  @Test
  public void testCreateDataSet() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    // check that auditing attributes have been set
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdAt", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedAt", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("system")))
      .andExpect(jsonPath("$.lastModifiedBy", is("system")));

    // call toString for test coverage :-)
    dataSet.toString();
  }

  @Test
  public void testCreateDataSetWithSurveyButWithoutProject() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(null, survey.getId());


    // Act and Assert
    // create the DataSet with a survey but without a project
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateDataSetWithUnknownSurvey() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), "notExist");

    // Act and Assert
    // create the DataSet with the given id but with an unknown survey
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void testCreateDataSetWithSurveyFromDifferentProject() throws Exception {

    // Arrange
    DataAcquisitionProject project1 = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    DataAcquisitionProject project2 = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project2.setId("testProject2");
    this.dataAcquisitionProjectRepository.save(project1);
    this.dataAcquisitionProjectRepository.save(project2);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project2.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project1.getId(), survey.getId());

    // Act and Assert
    // create the DataSet with the given id but with a survey from a different project
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void deleteDataSet() throws JsonSyntaxException, IOException, Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());

    // create the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    // delete the DataSet
    mockMvc.perform(delete(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the DataSet has been deleted
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isNotFound());
  }

  @Test
  public void testUpdateDataSet() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());

    // Act and Assert
    // create the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    dataSet.getDescription()
      .setDe("Angepasst.");

    // update the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().is2xxSuccessful());

    // read the updated DataSet and check the version
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(dataSet.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.description.de", is("Angepasst.")));
  }

  @Test
  public void testDeletingProjectDeletesDataSet() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);

    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());

    // Act and Assert
    // create the DataSet with the given id
    mockMvc.perform(put(API_DATASETS_URI + "/" + dataSet.getId())
      .content(TestUtil.convertObjectToJsonBytes(dataSet)))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data_acquisition_projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the DataSet has been deleted
    mockMvc.perform(get(API_DATASETS_URI + "/" + dataSet.getId()))
      .andExpect(status().isNotFound());
  }


}
