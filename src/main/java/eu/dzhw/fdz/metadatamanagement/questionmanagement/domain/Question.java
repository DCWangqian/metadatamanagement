package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionImageType;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Question.
 * 
 * @author Daniel Katzberg
 */
@Document(collection = "questions")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders")
public class Question extends AbstractRdcDomainObject {
  
  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "question-management.error.question.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "question-management.error.question.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "question-management.error.question.id.size")
  private String id;
  
  @NotEmpty(message = "question-management.error.question.number.not-empty")
  @Size(max = StringLengths.SMALL, message = "question-management.error.question.number.size")
  private String number;
  
  @NotNull(message = "question-management.error.question.question-text.not-null")
  @I18nStringSize(min = 1, max = StringLengths.LARGE, 
      message = "question-management.error.question.question-text.size")
  private I18nString questionText;
  
  @I18nStringSize(max = StringLengths.LARGE, 
      message = "question-management.error.question.instruction.size")
  private I18nString instruction;
  
  @I18nStringSize(max = StringLengths.LARGE, 
      message = "question-management.error.question.introduction.size")
  private I18nString introduction;
  
  @NotNull(message = "question-management.error.question.type.not-null")
  @ValidQuestionType(message = "question-management.error.question.type.valid-question-type")
  private I18nString type;
  
  @I18nStringSize(max = StringLengths.X_LARGE, 
      message = "question-management.error.question.introduction.size")
  private I18nString additionalQuestionText;
  
  @ValidQuestionImageType(
      message = "question-management.error.question.image-type.valid-question-image-type")
  @NotNull(message = "question-management.error.question.image-type.not-null")
  private ImageType imageType;
  
  @Size(max = StringLengths.X_LARGE, 
      message = "question-management.error.question.technical-representation.size")
  private String technicalRepresentation;
    
  private List<String> predecessor;
  
  private List<String> successor;
  
      
  /* Foreign Keys */
  @Indexed
  private String dataAcquisitionProjectId;
  
  @NotEmpty(message = "question-management.error.question.survey-id.not-empty")
  private String surveyId;
    
  private List<String> variableIds;
  
  @NotEmpty(message = "question-management.error.question.instrument-id.not-empty")
  private String instrumentId;
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#getId()
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
      .add("number", number)
      .add("questionText", questionText)
      .add("instruction", instruction)
      .add("introduction", introduction)
      .add("type", type)
      .add("additionalQuestionText", additionalQuestionText)
      .add("imageType", imageType)
      .add("technicalRepresentation", technicalRepresentation)
      .add("predecessor", predecessor)
      .add("successor", successor)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("surveyId", surveyId)
      .add("variableIds", variableIds)
      .add("instrumentId", instrumentId)
      .toString();
  }

  /* GETTER / SETTER */
  public void setId(String id) {
    this.id = id;
  }
  
  public String getNumber() {
    return number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public String getSurveyId() {
    return surveyId;
  }
  
  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }
  
  public I18nString getQuestionText() {
    return questionText;
  }
  
  public void setQuestionText(I18nString questionText) {
    this.questionText = questionText;
  }
  
  public I18nString getInstruction() {
    return instruction;
  }
  
  public void setInstruction(I18nString instruction) {
    this.instruction = instruction;
  }
  
  public I18nString getIntroduction() {
    return introduction;
  }
  
  public void setIntroduction(I18nString introduction) {
    this.introduction = introduction;
  }
  
  public I18nString getType() {
    return type;
  }
  
  public void setType(I18nString type) {
    this.type = type;
  }
  
  public I18nString getAdditionalQuestionText() {
    return additionalQuestionText;
  }
  
  public void setAdditionalQuestionText(I18nString additionalQuestionText) {
    this.additionalQuestionText = additionalQuestionText;
  }
    
  public ImageType getImageType() {
    return imageType;
  }

  public void setImageType(ImageType imageType) {
    this.imageType = imageType;
  }

  public String getTechnicalRepresentation() {
    return technicalRepresentation;
  }
  
  public void setTechnicalRepresentation(String technicalRepresentation) {
    this.technicalRepresentation = technicalRepresentation;
  }
  
  public List<String> getPredecessor() {
    return predecessor;
  }

  public void setPredecessor(List<String> predecessor) {
    this.predecessor = predecessor;
  }

  public List<String> getSuccessor() {
    return successor;
  }

  public void setSuccessor(List<String> successor) {
    this.successor = successor;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }
  
  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }
  
  public List<String> getVariableIds() {
    return variableIds;
  }
  
  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }
}
