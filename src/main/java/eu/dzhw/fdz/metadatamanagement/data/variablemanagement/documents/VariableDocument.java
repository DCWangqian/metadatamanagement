package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.UniqueAnswerCode;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidScaleLevel;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This is a representation of a variable. All fields describe the attributes of the variable, for
 * example the possible answers, the labels or the data type.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(
    indexName = "#{'" + AbstractDocument.METADATA_INDEX + "_'"
        + "+T(org.springframework.context.i18n.LocaleContextHolder).getLocale().getLanguage()}",
    type = "variables")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders")
public class VariableDocument extends AbstractDocument {

  // Basic Fields
  public static final Field ALL_STRINGS_AS_NGRAMS_FIELD =
      new Field("allStringsAsNgrams");
  public static final Field NAME_FIELD = new Field("name");
  public static final Field DATA_TYPE_FIELD = new Field("dataType");
  public static final Field LABEL_FIELD = new Field("label");
  public static final Field SCALE_LEVEL_FIELD =
      new Field("scaleLevel");
  public static final Field QUESTION_FIELD = new Field("question");
  public static final Field ANSWER_OPTIONS_FIELD = new Field("answerOptions");
  public static final Field VARIABLE_SURVEY_FIELD = new Field("variableSurvey");

  // Nested: Variable Document - Variable Survey
  public static final Field NESTED_VARIABLE_SURVEY_TITLE_FIELD =
      VARIABLE_SURVEY_FIELD.clone()
          .withNestedField(new Field(
              VARIABLE_SURVEY_FIELD.getPath() + "." + VariableSurvey.TITLE_FIELD.getPath()));

  public static final Field NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD =
      VARIABLE_SURVEY_FIELD.clone().withNestedField(new Field(
          VARIABLE_SURVEY_FIELD.getPath() + "." + VariableSurvey.VARIABLE_ALIAS_FIELD.getPath()));

  public static final Field NESTED_VARIABLE_SURVEY_ID_FIELD =
      VARIABLE_SURVEY_FIELD.clone().withNestedField(new Field(
          VARIABLE_SURVEY_FIELD.getPath() + "." + VariableSurvey.SURVEY_ID_FIELD.getPath()));

  public static final Field NESTED_VARIABLE_SURVEY_PERIOD_FIELD =
      VARIABLE_SURVEY_FIELD.clone().withNestedField(new Field(
          VARIABLE_SURVEY_FIELD.getPath() + "." + VariableSurvey.SURVEY_PERIOD_FIELD.getPath()));

  // Nested: Variable Document - Variable Survey - Survey Period
  public static final Field NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE;
  public static final Field NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE;

  static {
    // Start Date Range as third depth layer
    NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE = NESTED_VARIABLE_SURVEY_PERIOD_FIELD.clone();
    NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getNestedField()
        .withNestedField(
            new Field(NESTED_VARIABLE_SURVEY_PERIOD_FIELD.getNestedField().getNestedPath() + "."
                + DateRange.STARTDATE_FIELD.getPath()));

    // End Date Range as third depth layer
    NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE = NESTED_VARIABLE_SURVEY_PERIOD_FIELD.clone();
    NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getNestedField()
        .withNestedField(
            new Field(NESTED_VARIABLE_SURVEY_PERIOD_FIELD.getNestedField().getNestedPath() + "."
                + DateRange.ENDDATE_FIELD.getPath()));
  }

  // Nested: Variable Document - Answer Options
  public static final Field NESTED_ANSWER_OPTIONS_CODE_FIELD =
      ANSWER_OPTIONS_FIELD.clone().withNestedField(
          new Field(ANSWER_OPTIONS_FIELD.getPath() + "." + AnswerOption.CODE_FIELD.getPath()));

  public static final Field NESTED_ANSWER_OPTIONS_LABEL_FIELD =
      ANSWER_OPTIONS_FIELD.clone().withNestedField(
          new Field(ANSWER_OPTIONS_FIELD.getPath() + "." + AnswerOption.LABEL_FIELD.getPath()));

  /**
   * This is a nested reference to the survey.
   */
  @Valid
  @NotNull
  private VariableSurvey variableSurvey;

  /**
   * The name of the variable.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String name;

  /**
   * The data type of the variable.
   */
  @ValidDataType(groups = {Create.class, Edit.class})
  private String dataType;

  /**
   * The label of the variable.
   */
  @Size(max = 80, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String label;

  /**
   * This field holds the questions of the variable.
   */
  @Size(max = 256, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String question;

  /**
   * A optional scale level of the variable, if the variable is e.g. not a String.
   */
  /*
   * One more validation (must field if datatype is numeric. happens in the Validator
   * VariableDocumentValidator.
   */
  @ValidScaleLevel(groups = {Create.class, Edit.class})
  private String scaleLevel;

  /**
   * The value (answer options) with depending labels are represent in this nested field.
   */
  @Valid
  @UniqueAnswerCode(groups = {Create.class, Edit.class})
  private List<AnswerOption> answerOptions;

  /**
   * Create a variableDocument with a empty AnswerOption.
   */
  public VariableDocument() {
    this.answerOptions = new ArrayList<>();
    this.variableSurvey = new VariableSurvey();
  }

  /**
   * Adds a answer Option to the list.
   * 
   * @param answerOption an AnswerOption
   * @return Feedback for successful adding
   */
  public boolean addAnswerOption(AnswerOption answerOption) {

    // ignore null options
    if (answerOption == null) {
      return false;
    }

    return this.answerOptions.add(answerOption);
  }

  /**
   * Removes a answer Option from the list.
   * 
   * @param index an the index of the AnswerOption
   * @return The deleted AnswerOption
   */
  public AnswerOption removeAnswerOption(int index) {

    // ignore wrong index
    if (index < 0 || index >= this.answerOptions.size()) {
      return null;
    }

    return this.answerOptions.remove(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#toString()
   */
  @Override
  public String toString() {
    return "VariableDocument [variableSurvey=" + variableSurvey + ", name=" + name + ", dataType="
        + dataType + ", label=" + label + ", question=" + question + ", scaleLevel=" + scaleLevel
        + ", answerOptions=" + answerOptions + ", toString()=" + super.toString() + "]";
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), variableSurvey, name, dataType, label, question,
        scaleLevel, answerOptions);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }
      VariableDocument that = (VariableDocument) object;
      return Objects.equal(this.variableSurvey, that.variableSurvey)
          && Objects.equal(this.name, that.name) && Objects.equal(this.dataType, that.dataType)
          && Objects.equal(this.label, that.label) && Objects.equal(this.question, that.question)
          && Objects.equal(this.scaleLevel, that.scaleLevel)
          && Objects.equal(this.answerOptions, that.answerOptions);
    }
    return false;
  }

  /* GETTER / SETTER */
  public VariableSurvey getVariableSurvey() {
    return variableSurvey;
  }

  public void setVariableSurvey(VariableSurvey variableSurvey) {
    this.variableSurvey = variableSurvey;
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

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(String scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public List<AnswerOption> getAnswerOptions() {
    return answerOptions;
  }

  public void setAnswerOptions(List<AnswerOption> answerOptions) {
    this.answerOptions = answerOptions;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

}
