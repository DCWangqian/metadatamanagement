/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.DataType;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.ScaleLevel;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(VariableTest.class);

  private static Validator VALIDATOR;

  @BeforeClass
  public static void setUpValidatorAndDates() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    VALIDATOR = validatorFactory.getValidator();
  }

  @Test
  public void testEmptyInValidVariableDocument() {

    // Arrange
    VariableDocument variable = new VariableDocument();
    variable.setFdzId("");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variable);

    // Assert
    assertEquals(2, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testEmptyInValidVariable()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{org.hibernate.validator.constraints.NotEmpty.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testEmptyValidVariableDocument() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testInvalidName() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsTooLongAndThrowAnException.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidName()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testInvalidLabel() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setLabel("ThisLabelIsNotOkay.ItIsTooLongAndThrowsAnException."
        + "ButTheLabelLengthIsVeryLong.ItNeedsManyWordsForTheException.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidLabel()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testValidLabel() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setLabel("ThisLabelIsOkay.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testValidEnums() {

    // Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    variableDocument.setDataType(DataType.STRING);
    variableDocument.setScaleLevel(ScaleLevel.ORDINAL);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testInvalidLabelAtAnswerOption() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    AnswerOption answerOption = new AnswerOption();
    answerOption
        .setLabel("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption);
    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Arrange
    assertEquals(2, variableVialations.size());
    Iterator<ConstraintViolation<VariableDocument>> ite = variableVialations.iterator();

    // both messages found (it is not possible to expect an exact order of error messages
    boolean foundSize = false;
    boolean foundNotEmpty = false;
    while (ite.hasNext()) {
      String msgTemplate = ite.next().getMessageTemplate();
      if (msgTemplate.equals("{javax.validation.constraints.Size.message}") && !foundSize) {
        foundSize = true;
      }
      if (msgTemplate.equals("{org.hibernate.validator.constraints.NotEmpty.message}")
          && !foundNotEmpty) {
        foundNotEmpty = true;
      }
    }
    assertEquals(true, foundSize);
    assertEquals(true, foundNotEmpty);
  }


  @Test
  public void testInvalidCodeAtAnswerOption() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    AnswerOption answerOption = new AnswerOption();
    answerOption.setLabel("This label is okay.");
    answerOption
        .setCode("AddAExtraLabelForAnTestValidationError.AddAExtraLabelForAnTestValidationError.");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption);
    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidLabelAtAnswerOption() " + variableVialation.getMessageTemplate()
          + " -> " + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testValidAnswerOption() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    AnswerOption answerOption = new AnswerOption();
    answerOption.setCode("This code is okay.");
    answerOption.setLabel("Label is okay.");
    List<AnswerOption> answerOptions = new ArrayList<>();
    answerOptions.add(answerOption);
    variableDocument.setAnswerOptions(answerOptions);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
    assertEquals("Label is okay.", variableDocument.getAnswerOptions().get(0).getLabel());
    assertEquals("This code is okay.", variableDocument.getAnswerOptions().get(0).getCode());
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithEmptyFields() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurveyDocument variableSurveyDocument = new VariableSurveyDocument();
    variableDocument.setVariableSurveyDocument(variableSurveyDocument);

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(3, variableVialations.size());
    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidVariableDocumentSurveyWithEmptyFields()"
          + variableVialation.getMessageTemplate() + " -> " + variableVialation.getMessage());

      assertEquals("{org.hibernate.validator.constraints.NotEmpty.message}",
          variableVialation.getMessageTemplate());
    }
  }

  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidAlias() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurveyDocument variableSurveyDocument = new VariableSurveyDocument();
    variableDocument.setVariableSurveyDocument(variableSurveyDocument);
    variableDocument.getVariableSurveyDocument().setSurveyId("AliasIsOkay.");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument().setAlias("ThisAliasIsTooLong.ItWillThrowAnException");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidVariableDocumentSurveyWithInvalidSurveyId()"
          + variableVialation.getMessageTemplate() + " -> " + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }
  
  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidSurveyId() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurveyDocument variableSurveyDocument = new VariableSurveyDocument();
    variableDocument.setVariableSurveyDocument(variableSurveyDocument);
    variableDocument.getVariableSurveyDocument().setSurveyId("SurveyIdIsTooLong.ItWillThrowAnException");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidVariableDocumentSurveyWithInvalidSurveyId()"
          + variableVialation.getMessageTemplate() + " -> " + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }
  
  
  @Test
  public void testInvalidVariableDocumentSurveyWithInvalidTitle() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurveyDocument variableSurveyDocument = new VariableSurveyDocument();
    variableDocument.setVariableSurveyDocument(variableSurveyDocument);
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());
    variableDocument.getVariableSurveyDocument().setSurveyId("SurveyIdIsOkay.");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsNotOkay.TheTitleIsTooLong.");

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("variableSurveyTest()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals("{javax.validation.constraints.Size.message}",
          variableVialation.getMessageTemplate());
    }
  }
  
  
  @Test
  public void testValidVariableDocumentSurvey() {

    // Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");
    VariableSurveyDocument variableSurveyDocument = new VariableSurveyDocument();
    variableDocument.setVariableSurveyDocument(variableSurveyDocument);
    variableDocument.getVariableSurveyDocument().setSurveyId("SurveyIdIsOkay.");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());

    // Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // Assert
    assertEquals(0, variableVialations.size());
  }


  @Test
  public void testInvalidDateRange() {

    //Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");

    VariableSurveyDocument variableSurveyDocument = new VariableSurveyDocument();
    variableSurveyDocument.setSurveyId("SurveyIdIsOkay.");
    variableSurveyDocument.setTitle("TitleIsOkay.");
    variableSurveyDocument.setAlias(variableDocument.getName());

    DateRange surveyPeriod = new DateRange();
    surveyPeriod.setStartDate(LocalDate.now());
    surveyPeriod.setEndDate(surveyPeriod.getStartDate().minusDays(2));
    variableSurveyDocument.setSurveyPeriod(surveyPeriod);

    variableDocument.setVariableSurveyDocument(variableSurveyDocument);
    
    //Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    //Assert
    assertEquals(1, variableVialations.size());

    for (ConstraintViolation<VariableDocument> variableVialation : variableVialations) {

      LOGGER.debug("testInvalidDateRange()" + variableVialation.getMessageTemplate() + " -> "
          + variableVialation.getMessage());

      assertEquals(
          "{eu.dzhw.fdz.metadatamanagement.domain.variablemanagement."
              + "validator.annotations.ValidDateRange.message}",
          variableVialation.getMessageTemplate());
    }
  }
  
  @Test
  public void testValidDateRange() {

    //Assert
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ThisIDisOkay.");
    variableDocument.setName("ThisNameIsOkay.");

    VariableSurveyDocument variableSurveyDocument = new VariableSurveyDocument();
    variableSurveyDocument.setSurveyId("SurveyIdIsOkay.");
    variableSurveyDocument.setTitle("TitleIsOkay.");
    variableSurveyDocument.setAlias(variableDocument.getName());

    DateRange surveyPeriod = new DateRange();
    surveyPeriod.setStartDate(LocalDate.now());
    surveyPeriod.setEndDate(surveyPeriod.getStartDate().plusDays(2));
    variableSurveyDocument.setSurveyPeriod(surveyPeriod);

    variableDocument.setVariableSurveyDocument(variableSurveyDocument);
    
    //Act
    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    //Assert
    assertEquals(0, variableVialations.size());
  }

  @Test
  public void testEmptyDateRangeValidator() {

    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setFdzId("ID");
    variableDocument.setName("name");
    variableDocument.setVariableSurveyDocument(new VariableSurveyDocument());
    variableDocument.getVariableSurveyDocument().setSurveyId("ID_Survey");
    variableDocument.getVariableSurveyDocument().setTitle("TitleIsOkay.");
    variableDocument.getVariableSurveyDocument().setAlias(variableDocument.getName());

    // set a date, but without a start and end date
    variableDocument.getVariableSurveyDocument().setSurveyPeriod(new DateRange());


    Set<ConstraintViolation<VariableDocument>> variableVialations = VALIDATOR.validate(variableDocument);

    // everything okay
    assertEquals(0, variableVialations.size());
  }
  
  @Test
  public void testVariableDocumentToString() {
    //Arrange
    VariableDocument variableDocument = new VariableDocument();
    
    //Act
    
    //Assert
    assertEquals("VariableDocument [getFdzId()=null, getVariableSurveyDocument()=null, "
        + "getName()=null, getLabel()=null, getDataType()=null, getScaleLevel()=null, "
        + "getAnswerOptions()=null]",
        variableDocument.toString());
  }
  
  @Test
  public void testVariableDocumentWithSurvayToString() {
    //Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setVariableSurveyDocument(new VariableSurveyDocument());
    variableDocument.setAnswerOptions(new ArrayList<>());
    
    //Act
    
    //Assert
    assertEquals("VariableDocument [getFdzId()=null, getVariableSurveyDocument()=VariableSurveyDocument "
        + "[getSurveyId()=null, getTitle()=null, getDateRange()=null, getAlias()=null], "
        + "getName()=null, getLabel()=null, getDataType()=null, getScaleLevel()=null, "
        + "getAnswerOptions()=[]]", variableDocument.toString());
  }
  
  @Test
  public void testVariableDocumentWithSurveyAndEmptyDateRangeToString() {
    //Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setVariableSurveyDocument(new VariableSurveyDocument());
    variableDocument.setAnswerOptions(new ArrayList<>());
    variableDocument.getVariableSurveyDocument().setSurveyPeriod(new DateRange());
    
    //Act
    
    //Assert
    assertEquals(
        "VariableDocument [getFdzId()=null, getVariableSurveyDocument()=VariableSurveyDocument "
        + "[getSurveyId()=null, getTitle()=null, getDateRange()=null, getAlias()=null], "
        + "getName()=null, getLabel()=null, getDataType()=null, getScaleLevel()=null, "
        + "getAnswerOptions()=[]]",
        variableDocument.toString());
  }
  
  
  @Test
  public void testVariableDocumentWithSurveyAndFilledDateRangeToString() {
    //Arrange
    VariableDocument variableDocument = new VariableDocument();
    variableDocument.setVariableSurveyDocument(new VariableSurveyDocument());
    variableDocument.setAnswerOptions(new ArrayList<>());
    variableDocument.getVariableSurveyDocument().setSurveyPeriod(new DateRange());
    variableDocument.getVariableSurveyDocument().getSurveyPeriod().setStartDate(LocalDate.of(2015, 1, 1));
    variableDocument.getVariableSurveyDocument().getSurveyPeriod().setEndDate(LocalDate.of(2015, 2, 1));
    
    //Act
    
    //Assert
    assertEquals("VariableDocument [getFdzId()=null, getVariableSurveyDocument()=VariableSurveyDocument "
        + "[getSurveyId()=null, getTitle()=null, getDateRange()=null, getAlias()=null], getName()=null, "
        + "getLabel()=null, getDataType()=null, getScaleLevel()=null, getAnswerOptions()=[]]", 
        variableDocument.toString());
  }
}
