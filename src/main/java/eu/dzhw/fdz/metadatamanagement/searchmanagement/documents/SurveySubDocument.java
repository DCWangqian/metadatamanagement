package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Population;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;

/**
 * Attributes of a survey which are stored in other search documents.
 *
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
public class SurveySubDocument implements SurveySubDocumentProjection {
  private String id;

  private String dataAcquisitionProjectId;

  private Integer number;

  private Population population;

  private I18nString surveyMethod;

  private I18nString title;

  private Period fieldPeriod;

  private I18nString sample;

  private Integer wave;

  private I18nString dataType;

  public SurveySubDocument() {
    super();
  }

  public SurveySubDocument(SurveySubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  @Override
  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  @Override
  public Population getPopulation() {
    return population;
  }

  public void setPopulation(Population population) {
    this.population = population;
  }

  @Override
  public I18nString getSurveyMethod() {
    return surveyMethod;
  }

  public void setSurveyMethod(I18nString surveyMethod) {
    this.surveyMethod = surveyMethod;
  }

  @Override
  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  @Override
  public Period getFieldPeriod() {
    return fieldPeriod;
  }

  public void setFieldPeriod(Period fieldPeriod) {
    this.fieldPeriod = fieldPeriod;
  }

  @Override
  public I18nString getSample() {
    return sample;
  }

  public void setSample(I18nString sample) {
    this.sample = sample;
  }

  @Override
  public Integer getWave() {
    return wave;
  }

  public void setWave(Integer wave) {
    this.wave = wave;
  }

  @Override
  public I18nString getDataType() {
    return dataType;
  }

  public void setDataType(I18nString dataType) {
    this.dataType = dataType;
  }
}
