package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.DataType;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.ScaleLevel;

/**
 * This is a representation of a variable. All fields describe the attributes of the variable, for
 * example the possible answers, the labels or the data type.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(
    indexName = "#{'metadata_'"
        + "+T(org.springframework.context.i18n.LocaleContextHolder).getLocale().getLanguage()}",
    type = "variables")
public class VariableDocument {

  /**
   * A fdzID as primary key for the identification of the variable of a survey.
   */
  @Id
  @Size(max = 32)
  @NotEmpty
  private String fdzId;

  /**
   * This is a nested reference to the survey.
   */
  @Field(type = FieldType.Object)
  @Valid
  private VariableSurveyDocument variableSurveyDocument;

  /**
   * The name of the variable.
   */
  @Size(max = 32)
  @NotEmpty
  private String name;

  /**
   * The data type of the variable.
   */
  @Field(index = FieldIndex.not_analyzed)
  private DataType dataType;

  /**
   * The label of the variable.
   */
  @Size(max = 80)
  private String label;

  /**
   * A optional scale level of the variable, if the variable is e.g. not a String.
   */
  @Field(index = FieldIndex.not_analyzed)
  // TODO validate not null for variables with data type numeric
  private ScaleLevel scaleLevel;

  /**
   * The value (answer options) with depending labels are represent in this nested field.
   */
  @Field(type = FieldType.Object)
  @Valid
  private List<AnswerOption> answerOptions;

  /**
   * Output is a summarize of a variable. Example:
   * 
   * {@codeSurvey [fdzId=fdz123, SurveyVariableSurvey [surveyId=SId123, title=ExampleTitle,
   * DateRange [StartDate=2015-07-22, EndDate=2015-07-24]], name=ExampleName, dataType=NUMERIC,
   * label=ExampleLabel, scaleLevel=METRIC, AnswerOptions.size=6]}
   * 
   * @return A String which will summarizes the object date range.
   */
  @Override
  public String toString() {

    String variableSurvey = "null";
    if (this.getVariableSurveyDocument() != null) {
      variableSurvey = this.getVariableSurveyDocument().toString();
    }

    String answerOptionsSize = "0";
    if (this.getAnswerOptions() != null) {
      answerOptionsSize = this.getAnswerOptions().size() + "";
    }

    return "Survey [fdzId=" + this.getFdzId() + ", " + variableSurvey + ", " + "name="
        + this.getName() + ", dataType=" + this.getDataType() + ", label=" + this.getLabel()
        + ", scaleLevel=" + this.getScaleLevel() + ", AnswerOptions.size=" + answerOptionsSize
        + "]";
  }


  /* GETTER / SETTER */
  public String getFdzId() {
    return fdzId;
  }

  public void setFdzId(String fdzId) {
    this.fdzId = fdzId;
  }

  public VariableSurveyDocument getVariableSurveyDocument() {
    return variableSurveyDocument;
  }

  public void setVariableSurveyDocument(VariableSurveyDocument variableSurveyDocument) {
    this.variableSurveyDocument = variableSurveyDocument;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public ScaleLevel getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(ScaleLevel scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public List<AnswerOption> getAnswerOptions() {
    return answerOptions;
  }

  public void setAnswerOptions(List<AnswerOption> answerOptions) {
    this.answerOptions = answerOptions;
  }

}
