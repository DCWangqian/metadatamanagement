package eu.dzhw.fdz.metadatamanagement.domain;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The Data Acquisition Project collects all data which are going to be published by our RDC.
 * 
 * @author Daniel Katzberg
 */
@Document(collection = "data_acquisition_projects")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class DataAcquisitionProject extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "{error.dataAcquisitionProject.id.isEmpty}")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC, 
      message = "{error.dataAcquisitionProject.id.pattern}")
  @Size(max = StringLengths.SMALL, 
      message = "{error.dataAcquisitionProject.id.size}")
  private String id;

  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "{error.dataAcquisitionProject.surveySeries.i18nStringSize}")
  private I18nString surveySeries;

  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "{error.dataAcquisitionProject.panelName.i18nStringSize}")
  private I18nString panelName;

  /* Nested Objects */
  private List<Release> releases;

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
      .add("surveySeries", surveySeries)
      .add("panelName", panelName)
      .add("releases", releases)
      .toString();
  }

  /* GETTER / SETTER */
  public void setId(String id) {
    this.id = id;
  }

  public I18nString getSurveySeries() {
    return surveySeries;
  }

  public void setSurveySeries(I18nString surveySeries) {
    this.surveySeries = surveySeries;
  }

  public I18nString getPanelName() {
    return panelName;
  }

  public void setPanelName(I18nString panelName) {
    this.panelName = panelName;
  }

  public List<Release> getReleases() {
    return releases;
  }

  public void setReleases(List<Release> releases) {
    this.releases = releases;
  }
}
