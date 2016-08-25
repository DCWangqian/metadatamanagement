package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The Data Acquisition Project collects all data which are going to be published by our RDC.
 *
 * @author Daniel Katzberg
 */
@Document(collection = "data_acquisition_projects")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders")
public class DataAcquisitionProject extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "data-acquisition-project."
      + "error.data-acquisition-project.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC,
      message = "data-acquisition-project."
      + "error.data-acquisition-project.id.pattern")
  @Size(max = StringLengths.SMALL,
      message = "data-acquisition-project.error.data-acquisition-project.id.size")
  private String id;
  
  /* Foreign Keys */
  private String studyId;

  /*
   * (non-Javadoc)
   *
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }


  /*
   * (non-Javadoc)
   *
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("studyId", studyId)
      .toString();
  }

  /* GETTER / SETTER */
  public void setId(String id) {
    this.id = id;
  }


  public String getStudyId() {
    return studyId;
  }


  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }
}
