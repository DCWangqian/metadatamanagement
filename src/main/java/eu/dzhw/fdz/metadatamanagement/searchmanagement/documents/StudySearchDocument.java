package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of a study which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class StudySearchDocument extends Study implements SearchDocumentInterface {

  private static final long serialVersionUID = -4015837106569277626L;

  static final String[] FIELDS_TO_EXCLUDE_ON_DESERIALIZATION = new String[] {"nested*", "variables",
      "questions", "configuration", "guiLabels", "*Publications"};

  private List<DataSetSubDocument> dataSets = new ArrayList<>();
  private List<DataSetNestedDocument> nestedDataSets = new ArrayList<>();

  private List<VariableSubDocument> variables = new ArrayList<>();
  private List<VariableNestedDocument> nestedVariables = new ArrayList<>();

  private List<RelatedPublicationSubDocument> relatedPublications = new ArrayList<>();

  private List<RelatedPublicationNestedDocument> nestedRelatedPublications = new ArrayList<>();

  private List<SurveySubDocument> surveys = new ArrayList<>();
  private List<SurveyNestedDocument> nestedSurveys = new ArrayList<>();

  private List<QuestionSubDocument> questions = new ArrayList<>();
  private List<QuestionNestedDocument> nestedQuestions = new ArrayList<>();

  private List<InstrumentSubDocument> instruments = new ArrayList<>();
  private List<InstrumentNestedDocument> nestedInstruments = new ArrayList<>();

  private List<RelatedPublicationSubDocument> seriesPublications = new ArrayList<>();

  private List<ConceptSubDocument> concepts = new ArrayList<>();
  private List<ConceptNestedDocument> nestedConcepts = new ArrayList<>();

  private List<I18nString> nestedInstitutions = new ArrayList<>();

  private Release release = null;

  private Configuration configuration = null;

  private List<I18nString> surveyDataTypes;

  private Period surveyPeriod;

  private Integer numberOfWaves;

  private String doi;

  private I18nString guiLabels = StudyDetailsGuiLabels.GUI_LABELS;

  private I18nString completeTitle;

  /**
   * Construct the search document with all related subdocuments.
   * 
   * @param study The study to be searched for
   * @param dataSets all data sets available in this study
   * @param variables all variables available in this study
   * @param relatedPublications all related publications using this study
   * @param surveys all surveys available in this study
   * @param questions all questions available in this study
   * @param instruments all instruments available in this study
   * @param configuration the project configuration
   */
  @SuppressWarnings("CPD-START")
  public StudySearchDocument(Study study, List<DataSetSubDocumentProjection> dataSets,
      List<VariableSubDocumentProjection> variables,
      List<RelatedPublicationSubDocumentProjection> relatedPublications,
      List<SurveySubDocumentProjection> surveys, List<QuestionSubDocumentProjection> questions,
      List<InstrumentSubDocumentProjection> instruments,
      List<RelatedPublicationSubDocumentProjection> seriesPublications,
      List<ConceptSubDocumentProjection> concepts, Release release, String doi,
      Configuration configuration) {
    super(study);
    if (dataSets != null) {
      this.dataSets = dataSets.stream().map(DataSetSubDocument::new).collect(Collectors.toList());
      this.nestedDataSets =
          dataSets.stream().map(DataSetNestedDocument::new).collect(Collectors.toList());
    }
    if (variables != null) {
      this.variables =
          variables.stream().map(VariableSubDocument::new).collect(Collectors.toList());
      this.nestedVariables =
          variables.stream().map(VariableNestedDocument::new).collect(Collectors.toList());
    }
    if (relatedPublications != null) {
      this.relatedPublications = relatedPublications.stream()
          .map(RelatedPublicationSubDocument::new).collect(Collectors.toList());
      this.nestedRelatedPublications = relatedPublications.stream()
          .map(RelatedPublicationNestedDocument::new).collect(Collectors.toList());
    }
    if (surveys != null) {
      this.surveys = surveys.stream().map(SurveySubDocument::new).collect(Collectors.toList());
      this.nestedSurveys =
          surveys.stream().map(SurveyNestedDocument::new).collect(Collectors.toList());
      this.surveyDataTypes = generateSurveyDataTypes(surveys);
      this.numberOfWaves = generateNumberOfWaves(surveys);
      this.surveyPeriod = generateSurveyPeriod(surveys);
    }
    if (questions != null) {
      this.questions = questions.stream().map(question -> new QuestionSubDocument(question))
          .collect(Collectors.toList());
      this.nestedQuestions = questions.stream()
          .map(question -> new QuestionNestedDocument(question)).collect(Collectors.toList());
    }
    if (instruments != null) {
      this.instruments =
          instruments.stream().map(InstrumentSubDocument::new).collect(Collectors.toList());
      this.nestedInstruments =
          instruments.stream().map(InstrumentNestedDocument::new).collect(Collectors.toList());
    }
    if (seriesPublications != null) {
      this.seriesPublications = seriesPublications.stream().map(RelatedPublicationSubDocument::new)
          .collect(Collectors.toList());
    }
    if (concepts != null) {
      this.concepts = concepts.stream().map(concept -> new ConceptSubDocument(concept))
          .collect(Collectors.toList());
      this.nestedConcepts = concepts.stream().map(concept -> new ConceptNestedDocument(concept))
          .collect(Collectors.toList());
    }
    this.nestedInstitutions = study.getInstitutions();
    this.release = release;
    this.configuration = configuration;
    this.doi = doi;
    this.completeTitle = I18nString.builder()
        .de((study.getTitle().getDe() != null ? study.getTitle().getDe() : study.getTitle().getEn())
            + " (" + study.getId() + ")")
        .en((study.getTitle().getEn() != null ? study.getTitle().getEn() : study.getTitle().getDe())
            + " (" + study.getId() + ")")
        .build();
  }

  private Period generateSurveyPeriod(List<SurveySubDocumentProjection> surveys) {
    Period surveyPeriod = new Period();
    surveyPeriod.setStart(surveys.stream().map(SurveySubDocumentProjection::getFieldPeriod)
        .map(Period::getStart).min(LocalDate::compareTo).orElse(null));
    surveyPeriod.setEnd(surveys.stream().map(SurveySubDocumentProjection::getFieldPeriod)
        .map(Period::getEnd).max(LocalDate::compareTo).orElse(null));
    return surveyPeriod;
  }

  /**
   * Check the wave number of every survey.
   * 
   * @param surveys All Survey Sub Document Projections.
   * @return The highest (max) wave number.
   */
  private Integer generateNumberOfWaves(List<SurveySubDocumentProjection> surveys) {
    Integer numberOfWaves = 0;

    for (SurveySubDocumentProjection survey : surveys) {
      if (survey.getWave() > numberOfWaves) {
        numberOfWaves = survey.getWave();
      }
    }

    return numberOfWaves;
  }

  /**
   * Create an aggregated list of the data types of all {@link Survey}s.
   * 
   * @param surveys All Survey Sub Document Projections.
   * @return aggregated List of survey data types
   */
  private List<I18nString> generateSurveyDataTypes(List<SurveySubDocumentProjection> surveys) {
    return surveys.stream().map(SurveySubDocumentProjection::getDataType).distinct()
        .collect(Collectors.toList());
  }
}
