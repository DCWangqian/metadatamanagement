package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class StudyPublicListResourceTest extends AbstractTest {
  private static final String API_STUDY_URI = "/api/studies";
  @Autowired
  JestClient jestClient;
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
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).alwaysDo(print()).build();
    elasticsearchAdminService.recreateAllIndices();
  }

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    studyRepository.deleteAll();
     elasticsearchUpdateQueueItemRepository.deleteAll();
    javersService.deleteAll();
  }

  /**
   * add study search document to index with ID projectId.
   * 
   * @param projectId search doc ID and project Id
   * @param tile title of doc
   * @param isRelaesed true if released
   * @throws IOException if insert fails.
   */
  private void addStudyToIndex(String projectId, I18nString tile, boolean isRelaesed)
      throws IOException {
    Study buildStudy = UnitTestCreateDomainObjectUtils.buildStudy(projectId);
    buildStudy.setTitle(tile);
    buildStudy.setId(projectId);
    Configuration configuration = UnitTestCreateDomainObjectUtils
        .buildDataAcquisitionProjectConfiguration(Arrays.asList("publisher"), null);
    String doi = null;
    Release release = null;
    if (isRelaesed) {
      release = Release.builder().version("0.0.1").date(LocalDateTime.now()).build();
      doi = "doi";
    }
    StudySearchDocument doc = new StudySearchDocument(buildStudy, null, null, null, null, null,
        null, null, release, doi, configuration);
    Index build = new Index.Builder(doc).index("studies").type("studies").build();
    
    DocumentResult execute = jestClient.execute(build);
  }

  private void removeStudy(String id) throws IOException {
    jestClient.execute(new Delete.Builder(id).index("studies").type("studies").build());
  }

  @Test
  @Ignore
  public void test() throws Exception {
    String projectId = "test1";
    boolean isRelaesed = true;
    I18nString title = I18nString.builder().de("Test Studie").en("Test study").build();
    addStudyToIndex(projectId, title, isRelaesed);
    mockMvc.perform(get(API_STUDY_URI)).andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(projectId));
     removeStudy(projectId);
  }

}
