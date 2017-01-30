package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.MandatoryScaleLevelForNumericAndDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.OnlyOrdinalScaleLevelForDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsFirstQuartileMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsFirstQuartileMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMaximumMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMaximumMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMedianMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMedianMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMinimumMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMinimumMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsThirdQuartileMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueVariableIndexInDataSet;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueVariableNameInDataSet;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidAccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidScaleLevel;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidVariableIdName;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidVariableIdentifier;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Variable.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Document(collection = "variables")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
@CompoundIndexes({
    @CompoundIndex(def = "{name: 1, dataSetId: 1}", unique = true),
    @CompoundIndex(def = "{indexInDataSet: 1, dataSetId: 1}", unique = true)
    })
@ValidVariableIdName(message = "variable-management.error.variable.valid-variable-name")
@ValidVariableIdentifier(message = "variable-management.error.variable.valid-identifier")
@UniqueVariableNameInDataSet(message = "variable-management.error."
    + "variable.unique-variable-name-in-data-set")
@UniqueVariableIndexInDataSet(message = "variable-management.error."
    + "variable.unique-variable-index-in-dataSet")
@MandatoryScaleLevelForNumericAndDateDataType(
    message = "variable-management.error.variable.mandatory-scale-level-for"
    + "-numeric-and-date-data-type")
@OnlyOrdinalScaleLevelForDateDataType(
    message = "variable-management.error.variable.only-ordinal-scale-level-for-date-data-type")

//Validation if data type is date
@ValidResponseValueMustBeAnIsoDateOnDateDataType(
    message = "variable-management.error.variable."
        + "valid-response-value-must-be-an-iso-date-on-date-data-type")
@StatisticsMinimumMustBeAnIsoDateOnDateDataType(message = "variable-management.error.variable."
    + "statistics-minimum-must-be-an-iso-date-on-date-data-type")
@StatisticsMaximumMustBeAnIsoDateOnDateDataType(message = "variable-management.error.variable."
    + "statistics-maximum-must-be-an-iso-date-on-date-data-type")
@StatisticsMedianMustBeAnIsoDateOnDateDataType(message = "variable-management.error.variable."
    + "statistics-median-must-be-an-iso-date-on-date-data-type")
@StatisticsFirstQuartileMustBeAnIsoDateOnDateDataType(message = "variable-management.error."
    + "variable.statistics-first-quartile-must-be-an-iso-date-on-date-data-type")
@StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType(message = "variable-management.error."
    + "variable.statistics-third-quartile-must-be-an-iso-date-on-date-data-type")

//Validation if data type is numeric
@ValidResponseValueMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "valid-response-value-must-be-a-number-on-numeric-data-type")
@StatisticsMinimumMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-minimum-must-be-a-number-on-numeric-data-type")
@StatisticsMaximumMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-maximum-must-be-a-number-on-numeric-data-type")
@StatisticsMedianMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-median-must-be-a-number-on-numeric-data-type")
@StatisticsFirstQuartileMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-first-quartile-must-be-a-number-on-numeric-data-type")
@StatisticsThirdQuartileMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-third-quartile-must-be-a-number-on-numeric-data-type")
public class Variable extends AbstractRdcDomainObject {

  /* Domain Object listed attributes */
  @Id
  @NotEmpty(message = "variable-management.error.variable.id.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "variable-management.error.variable.id.pattern")
  private String id;

  @NotNull(message = "variable-management.error.variable.data-type.not-null")
  @ValidDataType(message = "variable-management.error.variable.data-type.valid-data-type")
  private I18nString dataType;

  @ValidScaleLevel(
      message = "variable-management.error.variable.scaleLevel.valid-scale-level")
  private I18nString scaleLevel;

  @NotEmpty(message = "variable-management.error.variable.name.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "variable-management.error.variable.name.size")
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN,
      message = "variable-management.error.variable.name.pattern")
  private String name;

  @NotNull(message = "variable-management.error.variable.label.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.label.i18n-string-size")
  @I18nStringNotEmpty(
      message = "variable-management.error.variable.label.i18n-string-not-empty")
  private I18nString label;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;

  // checks for min size too.
  @NotEmpty(message = "variable-management.error.variable.access-ways.not-empty")
  @ValidAccessWays(
      message = "variable-management.error.variable.access-ways.valid-access-ways")
  private List<String> accessWays;

  private List<String> relatedVariables;
  
  @NotEmpty(message = "variable-management.error.variable.data-set-id-not-empty")
  private String dataSetId;
  
  @NotNull(message = "variable-management.error.variable.data-set-number-not-null")
  private Integer dataSetNumber;
  
  @NotNull(message = "variable-management.error.variable.data-set-index-not-null")
  private Integer indexInDataSet;
  
  @NotEmpty(message = "variable-management.error.variable.survey-numbers-not-empty")
  private List<Integer> surveyNumbers;
 
  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.panel-identifier-size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_MINUS,
      message = "variable-management.error.variable.panel-identifier-pattern")
  private String panelIdentifier;


  /* Nested Objects */
  @Valid
  private FilterDetails filterDetails;

  @Valid
  private GenerationDetails generationDetails;

  @Valid
  private Distribution distribution;
  
  @Valid
  private List<RelatedQuestion> relatedQuestions;


  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "variable-management.error.variable.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  private List<String> surveyIds;

  /*
   * (non-Javadoc)
   *
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("dataType", dataType)
      .add("scaleLevel", scaleLevel)
      .add("name", name)
      .add("label", label)
      .add("annotations", annotations)
      .add("accessWays", accessWays)
      .add("relatedVariables", relatedVariables)
      .add("dataSetId", dataSetId)
      .add("dataSetNumber", dataSetNumber)
      .add("indexInDataSet", indexInDataSet)
      .add("surveyNumbers", surveyNumbers)
      .add("filterDetails", filterDetails)
      .add("generationDetails", generationDetails)
      .add("distribution", distribution)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("surveyIds", surveyIds)
      .add("relatedQuestions",relatedQuestions)
      .add("panelIdentifier", panelIdentifier)
      .toString();
  }



  /* GETTER / SETTER */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public I18nString getDataType() {
    return dataType;
  }

  public void setDataType(I18nString dataType) {
    this.dataType = dataType;
  }

  public I18nString getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(I18nString scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public I18nString getLabel() {
    return label;
  }

  public void setLabel(I18nString label) {
    this.label = label;
  }

  public I18nString getAnnotations() {
    return annotations;
  }

  public void setAnnotations(I18nString annotations) {
    this.annotations = annotations;
  }

  public List<String> getAccessWays() {
    return accessWays;
  }

  public void setAccessWays(List<String> accessWays) {
    this.accessWays = accessWays;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GenerationDetails getGenerationDetails() {
    return generationDetails;
  }

  public void setGenerationDetails(GenerationDetails generationDetails) {
    this.generationDetails = generationDetails;
  }

  public FilterDetails getFilterDetails() {
    return filterDetails;
  }

  public void setFilterDetails(FilterDetails filterDetails) {
    this.filterDetails = filterDetails;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }

  public Distribution getDistribution() {
    return distribution;
  }

  public void setDistribution(Distribution distribution) {
    this.distribution = distribution;
  }

  public List<String> getRelatedVariables() {
    return relatedVariables;
  }

  public void setRelatedVariables(List<String> relatedVariables) {
    this.relatedVariables = relatedVariables;
  }


  public String getDataSetId() {
    return dataSetId;
  }


  public void setDataSetId(String dataSetId) {
    this.dataSetId = dataSetId;
  }


  public Integer getDataSetNumber() {
    return dataSetNumber;
  }


  public void setDataSetNumber(Integer dataSetNumber) {
    this.dataSetNumber = dataSetNumber;
  }


  public Integer getIndexInDataSet() {
    return indexInDataSet;
  }


  public void setIndexInDataSet(Integer indexInDataSet) {
    this.indexInDataSet = indexInDataSet;
  }


  public List<Integer> getSurveyNumbers() {
    return surveyNumbers;
  }


  public void setSurveyNumbers(List<Integer> surveyNumbers) {
    this.surveyNumbers = surveyNumbers;
  }

  public List<RelatedQuestion> getRelatedQuestions() {
    return relatedQuestions;
  }
  
  public void setRelatedQuestions(List<RelatedQuestion> relatedQuestions) {
    this.relatedQuestions = relatedQuestions;
  }


  public String getPanelIdentifier() {
    return panelIdentifier;
  }


  public void setPanelIdentifier(String panelIdentifier) {
    this.panelIdentifier = panelIdentifier;
  }
  
}
