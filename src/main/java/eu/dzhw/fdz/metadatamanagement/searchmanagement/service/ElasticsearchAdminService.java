package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service which sets up all indices.
 * 
 * @author René Reitmann
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchAdminService {
  
  private final ElasticsearchDao elasticsearchDao;

  private final VariableRepository variableRepository;
  
  private final SurveyRepository surveyRepository;
  
  private final DataSetRepository dataSetRepository;
  
  private final QuestionRepository questionRepository;
  
  private final RelatedPublicationRepository relatedPublicationRepository;
  
  private final InstrumentRepository instrumentRepository;
  
  private final StudyRepository studyRepository;
  
  private final ConceptRepository conceptRepository;
  
  private final ElasticsearchUpdateQueueService updateQueueService;

  private final ResourceLoader resourceLoader;
  
  private JsonParser jsonParser = new JsonParser();

  /**
   * Recreate the indices and all their mappings.
   * Asynchronous cause it might take a while.
   */
  @Async
  public CompletableFuture<Object> recreateAllIndices() {
    try {
      for (ElasticsearchType type : ElasticsearchType.values()) {
        recreateIndex(type);
      }
      this.enqueueAllVariables();
      this.enqueueAllSurveys();
      this.enqueueAllDataSets();
      this.enqueueAllQuestions();
      this.enqueueAllRelatedPublications();
      this.enqueueAllInstruments();
      this.enqueueAllStudies();
      this.enqueueAllConcepts();
      updateQueueService.processAllQueueItems();
    } catch (Exception e) {
      log.error("Error during recreation of indices:", e);
    }
    return CompletableFuture.completedFuture(new Object());
  }
  
  private void enqueueAllStudies() {
    try (Stream<IdAndVersionProjection> studies = studyRepository.streamAllIdAndVersionsBy()) {
      studies.forEach(instrument -> {
        updateQueueService.enqueue(
            instrument.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
  
  private void enqueueAllConcepts() {
    try (Stream<IdAndVersionProjection> concepts = conceptRepository.streamAllIdAndVersionsBy()) {
      concepts.forEach(instrument -> {
        updateQueueService.enqueue(
            instrument.getId(), 
            ElasticsearchType.concepts, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
  
  private void enqueueAllInstruments() {
    try (Stream<IdAndVersionProjection> instruments = instrumentRepository
        .streamAllIdAndVersionsBy()) {
      instruments.forEach(instrument -> {
        updateQueueService.enqueue(
            instrument.getId(), 
            ElasticsearchType.instruments, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }

  /**
   * Load all variables from mongo and enqueue them for updating.
   */
  private void enqueueAllVariables() {
    try (Stream<IdAndVersionProjection> variables = variableRepository.streamAllIdAndVersionsBy()) {
      variables.forEach(variable -> {
        updateQueueService.enqueue(
            variable.getId(), 
            ElasticsearchType.variables, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
  
  /**
   * Load all surveys from mongo and enqueue them for updating.
   */
  private void enqueueAllSurveys() {
    try (Stream<IdAndVersionProjection> surveys = surveyRepository.streamAllIdAndVersionsBy()) {
      surveys.forEach(survey -> {
        updateQueueService.enqueue(
            survey.getId(), 
            ElasticsearchType.surveys, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
  
  /**
   * Load all dataSets from mongo and enqueue them for updating.
   */
  private void enqueueAllDataSets() {
    try (Stream<IdAndVersionProjection> dataSets = dataSetRepository.streamAllIdAndVersionsBy()) {
      dataSets.forEach(dataSet -> {
        updateQueueService.enqueue(
            dataSet.getId(), 
            ElasticsearchType.data_sets, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
    
  /**
   * Load all questions from mongo and enqueue them for updating.
   */
  private void enqueueAllQuestions() {
    try (Stream<IdAndVersionProjection> questions = questionRepository.streamAllIdAndVersionsBy()) {
      questions.forEach(question -> {
        updateQueueService.enqueue(
            question.getId(), 
            ElasticsearchType.questions, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
  
  /**
   * Load all related publications from mongo and enqueue them for updating.
   */
  private void enqueueAllRelatedPublications() {
    try (Stream<IdAndVersionProjection> relatedPublications = relatedPublicationRepository
        .streamAllIdAndVersionsBy()) {
      relatedPublications.forEach(relatedPublication -> {
        updateQueueService.enqueue(
            relatedPublication.getId(), 
            ElasticsearchType.related_publications, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      });
    }
  }

  /**
   * Deletes and create an elasticsearch index.
   * @param type name of the index (equals type for us).
   */
  private void recreateIndex(ElasticsearchType type) {
    if (elasticsearchDao.exists(type.name())) {
      elasticsearchDao.delete(type.name());
      // deleting is asynchronous and thus searchly complains if we create the new index to early
      elasticsearchDao.refresh(Arrays.asList(type.name()));
    }
    elasticsearchDao.createIndex(type.name(), loadSettings());
    elasticsearchDao.putMapping(type.name(), loadMapping(type.name()));
  }
  
  /**
   * Load Elasticsearch Index Settings.
   * @return A JSON Representation of the Settings.
   */
  @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  private JsonObject loadSettings() {
    try (InputStream inputStream = resourceLoader
          .getResource("classpath:elasticsearch/settings.json").getInputStream();
         Reader reader = new InputStreamReader(inputStream,"UTF-8");) {
      JsonObject settings = jsonParser.parse(reader)
          .getAsJsonObject();
      return settings;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load settings!", e);
    }
  }
  
  /**
   * Load Elasticsearch Mapping of an index.
   * @param type An elasticsearch type of an index.
   * @return A Json Representation of a Mapping
   */
  @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  private JsonObject loadMapping(String type) {
    try (InputStream inputStream = resourceLoader
        .getResource("classpath:elasticsearch/" + type + "/mapping.json")
        .getInputStream();
         Reader reader = new InputStreamReader(inputStream, "UTF-8");) {
      JsonObject mapping = jsonParser.parse(reader)
          .getAsJsonObject();
      return mapping;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load mapping for index " + type + " and type " + type,
          e);
    }
  }
  
  /**
   * Get the number of all documents in elastic search.
   * 
   * @return An Double Value with the number of count documents.
   */
  public Double countAllDocuments() {
    return elasticsearchDao.countAllDocuments();
  }
}
