package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidAccessWays;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The study domain object represents a study. A study can has more than one release. 
 * Every {@link DataAcquisitionProject} has exact one Study.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(collection = "studies")
@GeneratePojoBuilder(
     intoPackage = "eu.dzhw.fdz.metadatamanagement.studymanagement.domain.builders")
public class Study extends AbstractRdcDomainObject {
  
  private String id;
  
  @NotNull(message = "study-management.error.study.title.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.title.i18n-string-size")
  private I18nString title;
  
  @NotNull(message = "study-management.error.study.description.not-null")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "study-management.error.study.description.i18n-string-size")
  private I18nString descripion;
  
  @NotNull(message = "study-management.error.study.institution.not-null")
  @I18nStringSize(max = StringLengths.SMALL,
      message = "study-management.error.study.institution.i18n-string-size")
  private I18nString institution;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.survey-series.i18n-string-size")
  private I18nString surveySeries;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.sponsor.i18n-string-size")
  private I18nString sponsor;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "study-management.error.study.citation-hint.i18n-string-size")
  private I18nString citationHint;
  
  @Size(max = StringLengths.MEDIUM,
      message = "study-management.error.study.authors.i18n-string-size")
  private String authors;
  
  @NotEmpty(message = "study-management.error.study.access-ways.not-empty")
  @ValidAccessWays(
      message = "study-management.error.study.access-ways.valid-access-ways")
  private List<String> accessWays;

  /* Nested Objects */
  @Valid
  private List<Release> releases;
  
  /* Foreign Keys */
  private List<String> dataSetIds;
  
  private List<String> surveyIds;
  
  private List<String> instrumentIds;
  
  private List<String> relatedPublicationIds;
  
  @Indexed
  private String dataAcquisitionProjectId;
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("id", id)
      .add("title", title)
      .add("descripion", descripion)
      .add("institution", institution)
      .add("surveySeries", surveySeries)
      .add("sponsor", sponsor)
      .add("citationHint", citationHint)
      .add("authors", authors)
      .add("accessWays", accessWays)
      .add("releases", releases)
      .add("dataSetIds", dataSetIds)
      .add("surveyIds", surveyIds)
      .add("instrumentIds", instrumentIds)
      .add("relatedPublicationIds", relatedPublicationIds)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .toString();
  }

  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }


  /* GETTER / SETTER */
  public void setId(String id) {
    this.id = id;
  }
  
  public List<Release> getReleases() {
    return releases;
  }

  public void setReleases(List<Release> releases) {
    this.releases = releases;
  }
  
  public I18nString getSurveySeries() {
    return surveySeries;
  }

  public void setSurveySeries(I18nString surveySeries) {
    this.surveySeries = surveySeries;
  }

  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public I18nString getDescripion() {
    return descripion;
  }

  public void setDescripion(I18nString descripion) {
    this.descripion = descripion;
  }

  public I18nString getInstitution() {
    return institution;
  }

  public void setInstitution(I18nString institution) {
    this.institution = institution;
  }

  public I18nString getSponsor() {
    return sponsor;
  }

  public void setSponsor(I18nString sponsor) {
    this.sponsor = sponsor;
  }

  public I18nString getCitationHint() {
    return citationHint;
  }

  public void setCitationHint(I18nString citationHint) {
    this.citationHint = citationHint;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public List<String> getAccessWays() {
    return accessWays;
  }

  public void setAccessWays(List<String> accessWays) {
    this.accessWays = accessWays;
  }

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }

  public List<String> getSurveyIds() {
    return surveyIds;
  }

  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }

  public List<String> getInstrumentIds() {
    return instrumentIds;
  }

  public void setInstrumentIds(List<String> instrumentIds) {
    this.instrumentIds = instrumentIds;
  }

  public List<String> getRelatedPublicationIds() {
    return relatedPublicationIds;
  }

  public void setRelatedPublicationIds(List<String> relatedPublicationIds) {
    this.relatedPublicationIds = relatedPublicationIds;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }
}
