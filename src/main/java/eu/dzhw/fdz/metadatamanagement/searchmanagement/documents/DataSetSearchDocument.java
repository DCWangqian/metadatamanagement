package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a dataSet which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class DataSetSearchDocument extends DataSet implements SearchDocumentInterface {
  
  private StudySubDocument study = null;
  private List<VariableSubDocument> variables = 
      new ArrayList<>();
  private List<InstrumentSubDocument> instruments = 
      new ArrayList<>();
  private List<QuestionSubDocument> questions = 
      new ArrayList<>();
  private List<RelatedPublicationSubDocument> relatedPublications = 
      new ArrayList<>();
  private List<RelatedPublicationNestedDocument> nestedRelatedPublications = new ArrayList<>();
  private List<SurveySubDocument> surveys = 
      new ArrayList<>();
  private Release release = null;
  
  private Integer maxNumberOfObservations;
  
  private List<String> accessWays;
  
  private I18nString guiLabels = DataSetDetailsGuiLabels.GUI_LABELS;
  
  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   * @param dataSet The data set to be searched for.
   * @param study The study containing this data set.
   * @param variables The variables available in this data set.
   * @param relatedPublications The related publications using this data set.
   * @param surveys The surveys using this data set.
   * @param instruments The instruments used to create this data set.
   * @param questions The questions used to create this data set.
   */
  @SuppressWarnings("CPD-START")
  public DataSetSearchDocument(DataSet dataSet, 
      StudySubDocumentProjection study, 
      List<VariableSubDocumentProjection> variables,
      List<RelatedPublicationSubDocumentProjection> relatedPublications, 
      List<SurveySubDocumentProjection> surveys,
      Map<String, InstrumentSubDocumentProjection> instruments,
      List<QuestionSubDocumentProjection> questions,
      Release release,
      String doi) {
    super(dataSet);
    if (study != null) {
      this.study = new StudySubDocument(study, doi);            
    }
    if (variables != null) {
      this.variables = variables.stream()
          .map(VariableSubDocument::new).collect(Collectors.toList());      
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
      this.nestedRelatedPublications = relatedPublications.stream()
          .map(RelatedPublicationNestedDocument::new).collect(Collectors.toList());
    }
    if (surveys != null) {
      this.surveys = surveys.stream()
          .map(SurveySubDocument::new).collect(Collectors.toList());
    }
    if (instruments != null) {
      this.instruments = instruments.values().stream()
          .map(InstrumentSubDocument::new).collect(Collectors.toList());      
    }
    if (questions != null) {
      this.questions = questions.stream()
          .map(question -> new QuestionSubDocument(question,
              instruments.get(question.getInstrumentId()).getTitle()))
          .collect(Collectors.toList());
    }
    this.maxNumberOfObservations = dataSet.getSubDataSets().stream()
        .map(subDataSet -> subDataSet.getNumberOfObservations()).reduce(Integer::max).get();
    this.accessWays = dataSet.getSubDataSets().stream()
        .map(subDataSet -> subDataSet.getAccessWay()).collect(Collectors.toList());
    this.release = release;
    this.completeTitle = I18nString.builder()
        .de((dataSet.getDescription().getDe() != null ? dataSet.getDescription().getDe()
            : dataSet.getDescription().getEn()) + " (" + dataSet.getId() + ")")
        .en((dataSet.getDescription().getEn() != null ? dataSet.getDescription().getEn()
            : dataSet.getDescription().getDe()) + " (" + dataSet.getId() + ")")
        .build();
  }
}
