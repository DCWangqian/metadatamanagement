package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation.ValidStudyAttachmentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Metadata which will be stored with each attachment of a {@link Study}.
 */
@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class StudyAttachmentMetadata extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = -6838321823226222251L;

  /**
   * The id of the attachment. Holds the complete path which can be used to download the file.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of the study attachment.
   */
  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * The id of the {@link Study} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.study-id.not-empty")
  private String studyId;

  /**
   * The id of the {@link DataAcquisitionProject} to which the {@link Study} of this attachment
   * belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The index in the {@link Study} of this attachment. Used for sorting the attachments of this
   * {@link Study}.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "study-management.error.study-attachment-metadata.index-in-study.not-null")
  private Integer indexInStudy;

  /**
   * An optional title of this attachment in the attachments' language.
   *
   * Must not be empty and it must not contain more than 2048 characters.
   */
  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.title.not-null")
  @Size(max = StringLengths.LARGE, message =
      "study-management.error.study-attachment-metadata.title.string-size")
  private String title;

  /**
   * A description for this attachment.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message =
      "study-management.error.study-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "study-management.error.study-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "study-management.error.study-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  /**
   * The type of the attachment.
   *
   * Must be one of {@link StudyAttachmentTypes} and must not be empty.
   */
  @NotNull(message =
      "study-management.error.study-attachment-metadata.type.not-null")
  @I18nStringSize(min = 1, max = StringLengths.SMALL, message =
      "study-management.error.study-attachment-metadata.type.i18n-string-size")
  @ValidStudyAttachmentType(message =
      "study-management.error.study-attachment-metadata.type.valid-type")
  private I18nString type;

  /**
   * The filename of the attachment.
   *
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(message =
      "study-management.error.study-attachment-metadata.filename.not-empty")
  @Pattern(message =
      "study-management.error.study-attachment-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * The language of the attachments content.
   *
   * Must not be empty and must be specified as ISO 639 language code.
   */
  @NotNull(message =
      "study-management.error.study-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "study-management.error.study-attachment-metadata.language.not-supported")
  private String language;

  /**
   * Generate the id of this attachment from the studyId and the fileName.
   */
  public void generateId() {
    // hack to satisfy javers
    this.setId("/public/files/studies/" + studyId + "/attachments/" + fileName);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  /**
   * Returns the master id of the study attachment.
   * @return Master Id
   */
  @Override
  public String getMasterId() {
    return masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
