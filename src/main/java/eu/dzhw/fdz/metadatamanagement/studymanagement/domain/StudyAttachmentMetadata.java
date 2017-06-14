package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidStudyAttachmentType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Metadata which will be stored in GridFS with each attachment for studies.
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.studymanagement.domain.builders")
public class StudyAttachmentMetadata extends AbstractRdcDomainObject {
  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.study-id.not-empty")
  private String studyId;

  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  @NotNull(message =
      "study-management.error.study-attachment-metadata.index-in-study.not-null")
  private Integer indexInStudy;

  @Size(max = StringLengths.LARGE, message =
      "study-management.error.study-attachment-metadata.title.string-size")
  private String title;
  
  @NotNull(message =
      "study-management.error.study-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "study-management.error.study-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  @NotNull(message =
      "study-management.error.study-attachment-metadata.type.not-null")
  @I18nStringSize(min = 1, max = StringLengths.SMALL, message =
      "study-management.error.study-attachment-metadata.type.i18n-string-size")
  @ValidStudyAttachmentType(message =
      "study-management.error.study-attachment-metadata.type.valid-type")
  private I18nString type;

  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.filename.not-empty")
  private String fileName;

  @NotNull(message =
      "study-management.error.study-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "study-management.error.study-attachment-metadata.language.not-supported")
  private String language;

  public String getStudyId() {
    return studyId;
  }

  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public Integer getIndexInStudy() {
    return indexInStudy;
  }

  public void setIndexInStudy(Integer indexInStudy) {
    this.indexInStudy = indexInStudy;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public I18nString getType() {
    return type;
  }

  public void setType(I18nString type) {
    this.type = type;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public String getId() {
    return "/public/files/studies/" + studyId + "/attachments/" + fileName;
  }
  
}
