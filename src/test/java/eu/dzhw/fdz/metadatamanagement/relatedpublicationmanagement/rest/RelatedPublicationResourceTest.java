package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Collections;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * 
 * @author Daniel Katzberg
 *
 */
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class RelatedPublicationResourceTest extends AbstractTest {
  private static final String API_RELATED_PUBLICATION_URI = "/api/related-publications";
  
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private RelatedPublicationRepository publicationRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;
  
  @Autowired
  private JaversService javersService;
  
  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.publicationRepository.deleteAll();
    this.studyRepository.deleteAll();
    this.dataAcquisitionProjectRepository.deleteAll();
    this.elasticsearchUpdateQueueService.clearQueue();
    this.elasticsearchAdminService.recreateAllIndices();
    this.javersService.deleteAll();
  }
  
  @Test
  public void testCreateRelatedPublications() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    //ACT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isCreated());

    //ASSERT
    // read the related publication under the new url
    this.mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
      .andExpect(status().isOk());
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there is one data set document
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
  
  @Test
  public void testCreateRelatedPublicationsWithoutAuthors() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication(null, 2017);
    
    //ACT
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("related-publication-management.error.related-publication.authors.not-empty")));
  }
  
  @Test
  public void testCreateRelatedPublicationsWithInvalidYearInPast() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication("TestAuthors", 1959);
    
    //ACT
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("related-publication-management.error.related-publication.year.valid")));
  }
  
  @Test
  public void testCreateRelatedPublicationsWithInvalidYearInFuture() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    LocalDate date = new LocalDate();
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication("TestAuthors", date.getYear() + 1);
    
    //ACT
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("related-publication-management.error.related-publication.year.valid")));
  }
  
  @Test
  public void testUpdateRelatedPublications() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    //ACT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isCreated());
    
    relatedPublication.setDoi("Another DOI");
    
    //update the related publication with the given id
    mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().is2xxSuccessful());

    //ASSERT
    // read the related publication under the new url
    mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(relatedPublication.getId())))
      .andExpect(jsonPath("$.doi", is("Another DOI")));
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there are one data set document
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
  
  @Test
  public void testDeleteRelatedPublications() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    //ACT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().isCreated());
    
    // delete the related publication under the new url
    mockMvc.perform(delete(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId()))
      .andExpect(status().isNotFound());
    
    studyRepository.deleteById(study.getId());
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there are two data set documents plus two surveys
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }
  
  @Test
  public void testUpdateStudyWithInvalidReleaseDoi() throws IOException, Exception {
  //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    
    
    relatedPublication.setDoi("ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." + 
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." +
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." + 
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." +
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." + 
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." +
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong." + 
        "ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.ThisDoiIsTooLong.");
    
    //ACT and ASSERT
    // create the related publication with the given id
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
      .andExpect(status().is4xxClientError());
  }
  
  @Test
  public void testCreateRelatedPublicationWithInvalidStudySeries() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    I18nString studySeries = I18nString.builder().de("test").en("test").build();
    relatedPublication.setStudySerieses(Collections.singletonList(studySeries));
    
    // ACT and Assert
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
    .andExpect(status().isBadRequest())
    .andExpect(jsonPath("$.errors[0].message", 
        containsString("related-publication-management.error.related-publication.study-series-exists")));
  }
  
  @Test
  public void testCreateRelatedPublicationWithValidStudySeries() throws IOException, Exception {
    //ARRANGE
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    I18nString studySeries = I18nString.builder().de("test").en("test").build();
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    study.setStudySeries(studySeries);
    studyRepository.save(study);
    RelatedPublication relatedPublication = UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    relatedPublication.setStudySerieses(Collections.singletonList(studySeries));
    
    // ACT and Assert
    this.mockMvc.perform(put(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId())
      .content(TestUtil.convertObjectToJsonBytes(relatedPublication)))
    .andExpect(status().isCreated());
    
    // read the related publication under the new url
    mockMvc.perform(get(API_RELATED_PUBLICATION_URI + "/" + relatedPublication.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(relatedPublication.getId())))
      .andExpect(jsonPath("$.studySerieses[0].de", is(studySeries.getDe())))
      .andExpect(jsonPath("$.studySerieses[0].en", is(studySeries.getEn())));
    
    elasticsearchUpdateQueueService.processAllQueueItems();

    // check that there are two documents (study and related publication)
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
}
