package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation.ValidAccessWay;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Domain object of the SubDataSet.
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.builders")
public class SubDataSet {
  
  @NotEmpty(message = "data-set-management.error.sub-data-set.name.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "data-set-management.error.sub-data-set.name.size")
  private String name;
  
  private int numberOfObservations;  
  
  @NotNull(message = "data-set-management.error.sub-data-set.access-way.not-null")
  @ValidAccessWay(
      message = "data-set-management.error.sub-data-set.access-way.valid-access-way")
  private String accessWay;
  
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "data-set-management.error.sub-data-set.description.i18n-string-size")
  @I18nStringNotEmpty(
      message = "data-set-management.error.sub-data-set.description.i18n-string-not-empty")
  private I18nString description;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "data-set-management.error.sub-data-set.citation-hint.i18n-string-size")
  private I18nString citationHint;
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public int getNumberOfObservations() {
    return numberOfObservations;
  }
  
  public void setNumberOfObservations(int numberOfObservations) {
    this.numberOfObservations = numberOfObservations;
  }
  
  public String getAccessWay() {
    return accessWay;
  }
  
  public void setAccessWay(String accessWay) {
    this.accessWay = accessWay;
  }
  
  public I18nString getDescription() {
    return description;
  }
  
  public void setDescription(I18nString description) {
    this.description = description;
  }
  
  public I18nString getCitationHint() {
    return citationHint;
  }

  public void setCitationHint(I18nString citationHint) {
    this.citationHint = citationHint;
  }

  @Override
  public String toString() {
    return "SubDataSet [name=" + name + ", numberOfObservations=" + numberOfObservations
        + ", accessWay=" + accessWay + ", description=" + description + ", citationHint="
        + citationHint + "]";
  }

  

}
