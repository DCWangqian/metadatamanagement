package eu.dzhw.fdz.metadatamanagement.questionmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Service for creating and updating questions. Used for updating questions in mongo
 * and elasticsearch.
 */
@Service
@RepositoryEventHandler
public class QuestionService {

  @Autowired
  private QuestionRepository questionRepository;
 
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private QuestionImageService imageService;

  /**
   * Delete all questions when the dataAcquisitionProject was deleted.
   *
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteQuestionsByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * A service method for deletion of questions within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteQuestionsByProjectId(String dataAcquisitionProjectId) {
    List<Question> deletedQuestions = this.questionRepository
        .deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedQuestions.forEach(question -> {
      imageService.deleteQuestionImage(question.getId());
      elasticsearchUpdateQueueService.enqueue(
          question.getId(),
          ElasticsearchType.questions,
          ElasticsearchUpdateQueueAction.DELETE);
    });
  }

  /**
   * Enqueue deletion of question search document when the question is deleted.
   *
   * @param question the deleted question.
   */
  @HandleAfterDelete
  public void onQuestionDeleted(Question question) {
    imageService.deleteQuestionImage(question.getId());
    elasticsearchUpdateQueueService.enqueue(
        question.getId(),
        ElasticsearchType.questions,
        ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of question search document when the question is updated.
   *
   * @param question the updated or created question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onQuestionSaved(Question question) {
    elasticsearchUpdateQueueService.enqueue(
        question.getId(),
        ElasticsearchType.questions,
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Enqueue update of question search documents when the instrument is updated.
   * 
   * @param instrument the updated or created instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    Pageable page = new PageRequest(0, 100);
    Slice<Question> questions = questionRepository.findByInstrumentId(instrument.getId(), page);
    while (questions.hasContent()) {
      questions.forEach(question -> {
        elasticsearchUpdateQueueService.enqueue(
            question.getId(), 
            ElasticsearchType.questions, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      });
      page = page.next();
      questions = questionRepository.findByInstrumentId(instrument.getId(), page);
    }
  }
}
