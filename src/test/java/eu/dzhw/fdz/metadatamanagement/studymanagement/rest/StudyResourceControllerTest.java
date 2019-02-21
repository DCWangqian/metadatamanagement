package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 
 * @author Daniel Katzberg
 *
 */
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class StudyResourceControllerTest extends AbstractTest {
  private static final String API_STUDY_URI = "/api/studies";
  
  @Autowired
  private WebApplicationContext wac;
  
  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;
  
  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
    elasticsearchAdminService.recreateAllIndices();
  }

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    studyRepository.deleteAll();
    elasticsearchUpdateQueueItemRepository.deleteAll();
    javersService.deleteAll();
  }
  
  @Test
  public void testCreateStudy() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // read the study under the new url
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId()))
      .andExpect(status().isOk());
  }
  
  @Test
  public void testCreateStudyWithTooStudySeries() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
      study.setStudySeries(I18nString.builder()
          .de("Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn" +
              "Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn" +
                  "Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewg" +
                  "Zufallsstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
          .en("Randomstringhsdfosdhgfodsfvfsdhvdfaghscdqwdpqwubdpiempfuvgnrtgfnoeugfudgsfvoudgsvnauehgvenogfweuigfuzegnfvouiegsnfgaoseiufgvnuzewgfvnouagesfuenpvugfupewgn")
          .build());
      
    // create the project with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().is4xxClientError());
  }
   
  @Test
  public void testCreateStudyWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    this.dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    study.setId("hurz");
      
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().is4xxClientError());
  }
  
  @Test
  public void testUpdateStudy() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    
    // create the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
    
    List<Person> authors = new ArrayList<>();
    authors.add(UnitTestCreateDomainObjectUtils.buildPerson("Another", null, "Author"));
    study.setAuthors(authors);

    study.setVersion(0L);
    // update the study with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful());

    // read the study under the new url
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(study.getId())))
      .andExpect(jsonPath("$.authors[0].firstName", is("Another")))
      .andExpect(jsonPath("$.authors[0].lastName", is("Author")));
  }
  
  @Test
  public void testDeleteStudy() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    
    // create the project with the given id
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // delete the project under the new url
    mockMvc.perform(delete(API_STUDY_URI + "/" + study.getId()))
      .andExpect(status().is2xxSuccessful());

    // ensure it is really deleted
    mockMvc.perform(get(API_STUDY_URI + "/" + study.getId()))
      .andExpect(status().isNotFound());
  }
  
  @Test
  public void testUpdateStudyWithWrongId() throws IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    study.setId("ThisIdIsWrong.");
    
    // Try to put into mongo db
    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
      .content(TestUtil.convertObjectToJsonBytes(study)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testCreateShadowCopyStudy() throws Exception {
    Study study = UnitTestCreateDomainObjectUtils.buildStudy("issue1991");
    study.setId(study.getId() + "-1.0.0");

    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
        .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  public void testUpdateShadowCopyStudy() throws Exception {
    Study study = UnitTestCreateDomainObjectUtils.buildStudy("issue1991");
    study.setId(study.getId() + "-1.0.0");
    studyRepository.save(study);

    mockMvc.perform(put(API_STUDY_URI + "/" + study.getId())
        .content(TestUtil.convertObjectToJsonBytes(study)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-update-not-allowed")));
  }

  @Test
  public void testDeleteShadowCopyStudy() throws Exception {
    Study study = UnitTestCreateDomainObjectUtils.buildStudy("issue1991");
    study.setId(study.getId() + "-1.0.0");
    studyRepository.save(study);

    mockMvc.perform(delete(API_STUDY_URI + "/" + study.getId()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }
}
