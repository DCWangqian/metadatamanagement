package eu.dzhw.fdz.metadatamanagement;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DaraUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * This class is a basic class for the most unit tests.
 * 
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource("/config/application.yml")
@ActiveProfiles(Constants.SPRING_PROFILE_UNITTEST)
@WebAppConfiguration
public abstract class AbstractTest {

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private ConceptRepository conceptRepository;

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Autowired
  private ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  private DaraUpdateQueueItemRepository daraUpdateQueueItemRepository;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private Javers javers;

  @Autowired
  private TaskRepository taskRepository;

  @After
  public void ensureAllDataStoresHaveBeenCleanedUp() {
    assertEquals(0, this.studyRepository.count());
    assertEquals(0, this.surveyRepository.count());
    assertEquals(0, this.instrumentRepository.count());
    assertEquals(0, this.questionRepository.count());
    assertEquals(0, this.dataSetRepository.count());
    assertEquals(0, this.variableRepository.count());
    assertEquals(0, this.relatedPublicationRepository.count());
    assertEquals(0, this.conceptRepository.count());
    assertEquals(0, this.taskRepository.count());
    assertEquals(0, this.elasticsearchUpdateQueueItemRepository.count());
    assertEquals(0, this.daraUpdateQueueItemRepository.count());
    assertEquals(0, this.javers.findSnapshots(QueryBuilder.anyDomainObject().build()).size());
    assertThat(this.elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
    assertEquals(0, this.dataAcquisitionProjectRepository.count());
  }
}
