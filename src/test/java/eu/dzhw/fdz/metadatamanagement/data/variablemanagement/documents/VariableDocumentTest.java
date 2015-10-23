/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.DateRangeBuilder;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.builders.RelatedVariableBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.AnswerOptionBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableDocumentBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.VariableSurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.validators.VariableDocumentCreateValidator;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableDocumentTest extends AbstractTest {

  @Autowired
  private VariableDocumentCreateValidator variableDocumentCreateValidator;

  @Test
  public void testEmptyInvalidVariableDocument() {

    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(9, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NAME_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.QUESTION_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.ID_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError(VariableDocument.LABEL_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(
        errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath())
            .getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getAbsolutePath())
        .getCode(), is(NotNull.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getAbsolutePath())
        .getCode(), is(NotNull.class.getSimpleName()));
    assertThat(
        errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getAbsolutePath())
            .getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath())
        .getCode(), is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testMinimalValidVariableDocument() {

    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidID() {

    // Assert
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("This ID is not Okay")
        .withName("This Name Is Okay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.ID_FIELD.getAbsolutePath()).getCode(),
        is(Pattern.class.getSimpleName()));
  }

  @Test
  public void testValidIDWithSigns() {

    // Assert
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("This-ID_is-okay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withVariableSurvey(variableSurvey)
        .withQuestion("DefaultQuestion?").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidName() {

    // Assert
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsTooLongAndThrowAnException.").withLabel("LabelIsOkay")
        .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.NAME_FIELD.getAbsolutePath()).getCode(),
        is(Size.class.getSimpleName()));
  }

  @Test
  public void testInvalidLabel() {

    // Assert
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("ThisLabelIsNotOkay.ItIsTooLongAndThrowsAnException."
                + "ButTheLabelLengthIsVeryLong.ItNeedsManyWordsForTheException.")
        .withQuestion("DefaultQuestion?").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.LABEL_FIELD.getAbsolutePath()).getCode(),
        is(Size.class.getSimpleName()));
  }

  @Test
  public void testValidLabel() {

    // Assert
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("ThisLabelIsOkay")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testInvalidLabelAtAnswerOption() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(new AnswerOptionBuilder()
        .withLabel("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.")
        .build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withAnswerOptions(answerOptions).withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Arrange
    assertEquals(2, errors.getErrorCount());
    assertThat(errors.getFieldError("answerOptions[0].label").getCode(),
        is(Size.class.getSimpleName()));
    assertThat(errors.getFieldError("answerOptions[0].code").getCode(),
        is(NotNull.class.getSimpleName()));


  }

  @Test
  public void testInvalidCodeAtAnswerOption() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(null).build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withAnswerOptions(answerOptions).withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError("answerOptions[0].code").getCode(),
        is(NotNull.class.getSimpleName()));
  }

  @Test
  public void testValidAnswerOption() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(5).build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withAnswerOptions(answerOptions).withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
    assertEquals("This label is okay.", variableDocument.getAnswerOptions().get(0).getLabel());
    assertEquals(Integer.valueOf(5), variableDocument.getAnswerOptions().get(0).getCode());
  }

  @Test
  public void testValidRelatedVariables() {

    // Arrange
    List<RelatedVariable> relatedVariables = new ArrayList<>();
    relatedVariables.add(new RelatedVariableBuilder().withId("VID_RV").withName("VName_RV")
        .withLabel("VL_RV").withDataType(new DataTypesProvider().getNumericValueByLocale())
        .withScaleLevel(new ScaleLevelProvider().getOrdinalByLocal()).build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withRelatedVariables(relatedVariables).withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
    assertEquals("VL_RV", variableDocument.getRelatedVariables().get(0).getLabel());
    assertEquals("VName_RV", variableDocument.getRelatedVariables().get(0).getName());
  }

  @Test
  public void testInvalidRelatedVariables() {

    // Arrange
    List<RelatedVariable> relatedVariables = new ArrayList<>();
    relatedVariables.add(new RelatedVariableBuilder().withId("RV_ID").build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();

    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withRelatedVariables(relatedVariables).withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(2, errors.getErrorCount());
    assertThat(errors.getFieldError("relatedVariables[0].label").getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors.getFieldError("relatedVariables[0].name").getCode(),
        is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testInvalidAnswerOptionWithANullCode() {

    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(5).build());
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(null).build());
    answerOptions
        .add(new AnswerOptionBuilder().withLabel("This label is okay.").withCode(2).build());

    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withAnswerOptions(answerOptions).withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError("answerOptions[1].code").getCode(),
        is(NotNull.class.getSimpleName()));
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithEmptyFields() {

    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withLabel("LabelIsOkay").withQuestion("DefaultQuestion?")
        .withVariableSurvey(new VariableSurveyBuilder().build()).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(5, errors.getErrorCount());
    assertThat(
        errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath())
            .getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(
        errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getAbsolutePath())
            .getCode(),
        is(NotBlank.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE.getAbsolutePath())
        .getCode(), is(NotNull.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE.getAbsolutePath())
        .getCode(), is(NotNull.class.getSimpleName()));
    assertThat(errors
        .getFieldError(
            VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath())
        .getCode(), is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidAlias() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay")
        .withTitle("TitleIsOkay").withVariableAlias("ThisAliasIsTooLong.ItWillThrowAnException")
        .withSurveyPeriod(new DateRangeBuilder().withStartDate(LocalDate.now())
            .withEndDate(LocalDate.now().plusDays(1)).build())
        .build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors
        .getFieldError(
            VariableDocument.NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD.getAbsolutePath())
        .getCode(), is(Size.class.getSimpleName()));
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidSurveyId() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder()
        .withSurveyId("SurveyIdIsTooLong.ItWillThrowAnException").withTitle("TitleIsOkay")
        .withVariableAlias("TitleIsOkay.").withSurveyPeriod(new DateRangeBuilder()
            .withStartDate(LocalDate.now()).withEndDate(LocalDate.now().plusDays(1)).build())
        .build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(
        errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_ID_FIELD.getAbsolutePath())
            .getCode(),
        is(Size.class.getSimpleName()));
  }


  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidTitle() {

    // Arrange
    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay.")
        .withTitle("TitleIsNotOkay.TheTitleIsTooLong.")
        .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(new DateRangeBuilder()
            .withStartDate(LocalDate.now()).withEndDate(LocalDate.now().plusDays(1)).build())
        .build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(
        errors.getFieldError(VariableDocument.NESTED_VARIABLE_SURVEY_TITLE_FIELD.getAbsolutePath())
            .getCode(),
        is(Size.class.getSimpleName()));
  }

  @Test
  public void testValidVariableDocumentSurvey() {

    // Arrange
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("SurveyIdIsOkay.").withTitle("TitleIsOkay.")
            .withVariableAlias("ThisNameIsOkay").withSurveyPeriod(new DateRangeBuilder()
                .withStartDate(LocalDate.now()).withEndDate(LocalDate.now().plusDays(1)).build())
        .build();

    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withVariableSurvey(variableSurvey).build();
    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(0, errors.getErrorCount());
  }

  @Test
  public void testVariableDocumentToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder().build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument{super=VariableDocument{id=[null]}, variableSurvey=VariableSurvey"
            + "{super=VariableSurvey{surveyId=null, title=null, surveyPeriod=DateRange{startDate=null, "
            + "endDate=null}}, variableAlias=null}, name=null, dataType=null, label=null, question=null, "
            + "scaleLevel=null, answerOptions=[], relatedVariables=[]}",
        variableDocument.toString());
  }

  @Test
  public void testVariableDocumentWithSurvayToString() {
    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withAnswerOptions(new ArrayList<>())
            .withVariableSurvey(new VariableSurveyBuilder().build()).build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument{super=VariableDocument{id=[null]}, variableSurvey=VariableSurvey{super=VariableSurvey{surveyId=null, "
            + "title=null, surveyPeriod=DateRange{startDate=null, endDate=null}}, variableAlias=null}, name=null, dataType=null, "
            + "label=null, question=null, scaleLevel=null, answerOptions=[], relatedVariables=[]}",
        variableDocument.toString());
  }

  @Test
  public void testVariableDocumentWithSurveyAndEmptyDateRangeToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withAnswerOptions(new ArrayList<>())
        .withVariableSurvey(
            new VariableSurveyBuilder().withSurveyPeriod(new DateRangeBuilder().build()).build())
        .build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument{super=VariableDocument{id=[null]}, variableSurvey=VariableSurvey"
            + "{super=VariableSurvey{surveyId=null, title=null, surveyPeriod=DateRange{startDate=null, "
            + "endDate=null}}, variableAlias=null}, name=null, dataType=null, label=null, question=null, "
            + "scaleLevel=null, answerOptions=[], relatedVariables=[]}",
        variableDocument.toString());
  }


  @Test
  public void testVariableDocumentWithSurveyAndFilledDateRangeToString() {
    // Arrange
    VariableDocument variableDocument = new VariableDocumentBuilder()
        .withAnswerOptions(new ArrayList<>())
        .withVariableSurvey(new VariableSurveyBuilder().withSurveyPeriod(new DateRangeBuilder()
            .withStartDate(LocalDate.of(2015, 1, 1)).withEndDate(LocalDate.of(2015, 2, 1)).build())
            .build())
        .build();

    // Act

    // Assert
    assertEquals(
        "VariableDocument{super=VariableDocument{id=[null]}, variableSurvey=VariableSurvey"
            + "{super=VariableSurvey{surveyId=null, title=null, surveyPeriod=DateRange{startDate=2015-01-01, "
            + "endDate=2015-02-01}}, variableAlias=null}, name=null, dataType=null, label=null, question=null, "
            + "scaleLevel=null, answerOptions=[], relatedVariables=[]}",
        variableDocument.toString());
  }

  @Test
  public void testInvalidVariableDocumentWithEmptyQuestion() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withLabel("LabelIsOkay")
            .withName("ThisNameIsOkay.").withVariableSurvey(variableSurvey).build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.QUESTION_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testInvalidVariableDocumentWithEmptyLabel() {
    // Arrange
    DateRange dateRange = new DateRangeBuilder().withStartDate(LocalDate.now())
        .withEndDate(LocalDate.now().plusDays(2)).build();
    VariableSurvey variableSurvey =
        new VariableSurveyBuilder().withSurveyId("Survey_ID").withTitle("Title 1")
            .withVariableAlias("VariableAlias 1").withSurveyPeriod(dateRange).build();
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withQuestion("Question.")
            .withVariableSurvey(variableSurvey).withName("ThisNameIsOkay.").build();

    // Act
    Errors errors = new BeanPropertyBindingResult(variableDocument, "variableDocument");
    this.variableDocumentCreateValidator.validate(variableDocument, errors);

    // Assert
    assertEquals(1, errors.getErrorCount());
    assertThat(errors.getFieldError(VariableDocument.LABEL_FIELD.getAbsolutePath()).getCode(),
        is(NotBlank.class.getSimpleName()));
  }

  @Test
  public void testRemoveAnswerOption() {
    // Arrange
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(new AnswerOptionBuilder().withCode(1).withLabel("Label 1").build());
    answerOptions.add(new AnswerOptionBuilder().withCode(2).build());
    answerOptions.add(new AnswerOptionBuilder().withCode(3).withLabel("Label 3").build());
    VariableDocument variableDocument = new VariableDocumentBuilder().withId("ThisIDisOkay")
        .withName("ThisNameIsOkay.").withQuestion("DefaultQuestion?").withLabel("LabelIsOkay")
        .withAnswerOptions(answerOptions).build();

    // Act
    AnswerOption answerOption1 = variableDocument.removeAnswerOption(1);
    AnswerOption answerOption2 = variableDocument.removeAnswerOption(1);
    AnswerOption answerOption3 = variableDocument.removeAnswerOption(4);
    AnswerOption answerOption4 = variableDocument.removeAnswerOption(-10);

    // Assert
    assertEquals(new Integer("2"), answerOption1.getCode());
    assertEquals(new Integer("3"), answerOption2.getCode());
    assertEquals("Label 3", answerOption2.getLabel());
    assertEquals(1, variableDocument.getAnswerOptions().size());
    assertEquals(null, answerOption3);
    assertEquals(null, answerOption4);
  }

  @Test
  public void testAddAnswerOption() {
    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("LabelIsOkay").withQuestion("DefaultQuestion?").build();

    // Act
    boolean checkNull = variableDocument.addAnswerOption(null);
    boolean checkValidCompleteAnswerOption = variableDocument
        .addAnswerOption(new AnswerOptionBuilder().withCode(1).withLabel("Label").build());
    boolean checkEmptyAnswerOption =
        variableDocument.addAnswerOption(new AnswerOptionBuilder().build());

    // Assert
    assertEquals(false, checkNull);
    assertEquals(true, checkValidCompleteAnswerOption);
    assertEquals(true, checkEmptyAnswerOption);
  }

  @Test
  public void testHashCode() {
    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("LabelIsOkay").withQuestion("DefaultQuestion?").build();
    VariableDocument variableDocument2 =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withQuestion("DefaultQuestion?").withAnswerOptions(null).build();

    // Act

    // Assert
    assertThat(variableDocument.hashCode(), not(0));
    assertThat(variableDocument2.hashCode(), not(0));
  }

  @Test
  public void testEquals() {
    // Arrange
    VariableDocument variableDocument =
        new VariableDocumentBuilder().withId("ThisIDisOkay").withName("ThisNameIsOkay.")
            .withLabel("LabelIsOkay").withQuestion("DefaultQuestion?").build();
    VariableDocument variableDocument2 = new VariableDocumentBuilder().build();

    VariableSurvey variableSurvey = new VariableSurveyBuilder().withSurveyId("ID1").build();
    VariableSurvey variableSurvey2 = new VariableSurveyBuilder().withSurveyId("ID2").build();

    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(new AnswerOptionBuilder().withCode(1).withLabel("Label1").build());
    List<AnswerOption> answerOptions2 = new ArrayList<>();
    answerOptions2.add(new AnswerOptionBuilder().withCode(2).withLabel("Label2").build());

    // Act
    boolean checkNull = variableDocument.equals(null);
    boolean checkDifferentClass = variableDocument.equals(new Object());
    boolean checkDifferentVariableDocument = variableDocument.equals(variableDocument2);
    boolean checkSame = variableDocument.equals(variableDocument);
    variableDocument.setVariableSurvey(variableSurvey);
    variableDocument2.setVariableSurvey(variableSurvey2);
    boolean checkDifferentSurvey = variableDocument.equals(variableDocument2);
    variableDocument2.setVariableSurvey(variableSurvey);
    variableDocument.setName("Name1");
    variableDocument2.setName("Name2");
    boolean checkDifferentName = variableDocument.equals(variableDocument2);
    variableDocument2.setName("Name1");
    variableDocument.setLabel("Label1");
    variableDocument2.setLabel("Label2");
    boolean checkDifferentLabel = variableDocument.equals(variableDocument2);
    variableDocument2.setLabel("Label1");
    variableDocument.setScaleLevel("metric");
    variableDocument2.setScaleLevel("ordinal");
    boolean checkDifferentScaleLevel = variableDocument.equals(variableDocument2);
    variableDocument2.setScaleLevel("metric");
    variableDocument.setAnswerOptions(answerOptions);
    variableDocument2.setAnswerOptions(answerOptions2);
    boolean checkDifferentAnswerOptions = variableDocument.equals(variableDocument2);

    // Assert
    assertEquals(false, checkNull);
    assertEquals(false, checkDifferentClass);
    assertEquals(false, checkDifferentVariableDocument);
    assertEquals(true, checkSame);
    assertEquals(false, checkDifferentSurvey);
    assertEquals(false, checkDifferentName);
    assertEquals(false, checkDifferentLabel);
    assertEquals(false, checkDifferentScaleLevel);
    assertEquals(false, checkDifferentAnswerOptions);
  }

  @Test
  public void testDepthFieldSurveyPeriod() {
    // Arrange
    DocumentField variableDocumentSurveyStartDate =
        VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE;
    DocumentField variableDocumentSurveyEndDate =
        VariableDocument.NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE;

    // Act
    DocumentField surveyPeriodOfStartDate = variableDocumentSurveyStartDate.getParent();
    DocumentField variableSurveyOfStartDate = surveyPeriodOfStartDate.getParent();
    DocumentField surveyPeriodOfEndDate = variableDocumentSurveyEndDate.getParent();
    DocumentField variableSurveyOfEndDate = surveyPeriodOfEndDate.getParent();

    // Assert
    assertThat(variableDocumentSurveyStartDate.getAbsolutePath(),
        is("variableSurvey.surveyPeriod.startDate"));
    assertThat(variableDocumentSurveyStartDate.getRelativePath(), is("startDate"));
    assertThat(surveyPeriodOfStartDate.getAbsolutePath(), is("variableSurvey.surveyPeriod"));
    assertThat(surveyPeriodOfStartDate.getRelativePath(), is("surveyPeriod"));
    assertThat(variableSurveyOfStartDate.getAbsolutePath(), is("variableSurvey"));
    assertThat(variableSurveyOfStartDate.getRelativePath(), is("variableSurvey"));

    assertThat(variableDocumentSurveyEndDate.getAbsolutePath(),
        is("variableSurvey.surveyPeriod.endDate"));
    assertThat(variableDocumentSurveyEndDate.getRelativePath(), is("endDate"));
    assertThat(surveyPeriodOfEndDate.getAbsolutePath(), is("variableSurvey.surveyPeriod"));
    assertThat(variableSurveyOfEndDate.getAbsolutePath(), is("variableSurvey"));
  }

  @Test
  public void testisHighlighted() {
    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    Map<String, String> map = new HashMap<>();

    // Act
    map.put("Highlight1", "Value1");
    variableDocument.setHighlightedFields(map);

    // Assert
    assertThat(variableDocument.getHighlightedFields().size(), is(1));
    assertThat(variableDocument.getHighlightedFields().get("Highlight1"), is("Value1"));
    assertThat(variableDocument.getHighlightedFields().get("Highlight2"), is(nullValue()));
    assertThat(variableDocument.isFieldHighlighted("Highlight1"), is(true));
    assertThat(variableDocument.isFieldHighlighted("Highlight2"), is(false));
  }
}
