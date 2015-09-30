package eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This is a representation of a question. All fields describe the attributes of the question, for
 * example the possible answers, the questionnaire or the study.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(
    indexName = "#{'" + AbstractDocument.METADATA_INDEX + "_'"
        + "+T(org.springframework.context.i18n.LocaleContextHolder).getLocale().getLanguage()}",
    type = "questions")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.questionmanagement.documents.builders")
public class QuestionDocument extends AbstractDocument {

  // Basic Fields
  public static final DocumentField QUESTION_SURVEY_FIELD = new DocumentField("questionSurvey");
  public static final DocumentField QUESTION_FIELD = new DocumentField("question");
  public static final DocumentField NAME_FIELD = new DocumentField("name");
  public static final DocumentField VARIABLE_DOCUMENTS_FIELD =
      new DocumentField("variableDocuments");

  // Nested Fields: Question Document - Question Survey
  public static final DocumentField NESTED_QUESTION_SURVEY_TITLE_FIELD =
      new DocumentField(QuestionDocument.QUESTION_SURVEY_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + QuestionSurvey.TITLE_FIELD);

  public static final DocumentField NESTED_QUESTION_SURVEY_ID_FIELD =
      new DocumentField(QuestionDocument.QUESTION_SURVEY_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + QuestionSurvey.SURVEY_ID_FIELD);

  public static final DocumentField NESTED_QUESTION_SURVEY_PERIOD_FIELD =
      new DocumentField(QuestionDocument.QUESTION_SURVEY_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + QuestionSurvey.SURVEY_PERIOD_FIELD);

  // Nested: Question Document - Question Survey - Survey Period
  public static final DocumentField NESTED_QUESTION_SURVEY_NESTED_PERIOD_START_DATE =
      new DocumentField(NESTED_QUESTION_SURVEY_PERIOD_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + DateRange.STARTDATE_FIELD);

  public static final DocumentField NESTED_QUESTION_SURVEY_NESTED_PERIOD_END_DATE =
      new DocumentField(NESTED_QUESTION_SURVEY_PERIOD_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + DateRange.ENDDATE_FIELD);
  
  //TODO Question: Nested Document Fields for the variable Document? Same question for mapping?

  /**
   * The question survey. It has some information about the survey which has this question.
   */
  @Valid
  @NotNull
  private QuestionSurvey questionSurvey;

  /**
   * The question as a String.
   */
  @Size(max = 256)
  @NotBlank
  private String question;

  /**
   * The name of the variable.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String name;

  /**
   * A list of all variables which are depending to this question.
   */
  @Valid
  private List<QuestionVariable> questionVariables;

  /**
   * Creates a question with empty survey and list of variable documents.
   */
  public QuestionDocument() {
    super();
    this.questionSurvey = new QuestionSurvey();
    this.questionVariables = new ArrayList<>();
  }


  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), questionSurvey, question, name, questionVariables);
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
      QuestionDocument that = (QuestionDocument) object;
      return Objects.equal(this.questionSurvey, that.questionSurvey)
          && Objects.equal(this.question, that.question) && Objects.equal(this.name, that.name)
          && Objects.equal(this.questionVariables, that.questionVariables);
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("super", super.toString())
        .add("questionSurvey", questionSurvey).add("question", question).add("name", name)
        .add("questionVariables", questionVariables).toString();
  }


  /* GETTER / SETTER */
  public QuestionSurvey getQuestionSurvey() {
    return questionSurvey;
  }

  public void setQuestionSurvey(QuestionSurvey questionSurvey) {
    this.questionSurvey = questionSurvey;
  }

  public List<QuestionVariable> getQuestionVariables() {
    return questionVariables;
  }

  public void setQuestionVariables(List<QuestionVariable> questionVariables) {
    this.questionVariables = questionVariables;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
