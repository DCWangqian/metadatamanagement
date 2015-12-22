package eu.dzhw.fdz.metadatamanagement.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.FdzProjectExists;
import eu.dzhw.fdz.metadatamanagement.domain.validation.SurveyExists;
import eu.dzhw.fdz.metadatamanagement.domain.validation.ValidSurveyFdzProjectRelation;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Variable.
 */
@Document(collection = "variable")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
@CompoundIndex(def = "{name: 1, fdz_project_name: 1}", unique = true)
@ValidSurveyFdzProjectRelation
public class Variable implements Serializable {

  private static final long serialVersionUID = 3447432736734388659L;

  @Id
  @NotBlank
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE)
  private String id;

  @NotBlank
  @Size(max = 32)
  @Field("name")
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE)
  private String name;

  @NotNull
  @Field("data_type")
  private DataType dataType;

  @NotNull
  @Field("scale_level")
  private ScaleLevel scaleLevel;

  @NotBlank
  @Size(max = 128)
  @Field("label")
  private String label;

  @FdzProjectExists
  @NotNull
  @Field("fdz_project_name")
  private String fdzProjectName;
  
  @SurveyExists
  @Field("survey_id")
  private String surveyId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public ScaleLevel getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(ScaleLevel scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getFdzProjectName() {
    return fdzProjectName;
  }

  public String getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }

  public void setFdzProjectName(String fdzProjectName) {
    this.fdzProjectName = fdzProjectName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    Variable variable = (Variable) object;

    if (!Objects.equals(id, variable.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Variable [id=" + id + ", name=" + name + ", dataType=" + dataType + ", scaleLevel="
        + scaleLevel + ", label=" + label + ", fdzProjectName=" + fdzProjectName + ", surveyId="
        + surveyId + "]";
  }
}
