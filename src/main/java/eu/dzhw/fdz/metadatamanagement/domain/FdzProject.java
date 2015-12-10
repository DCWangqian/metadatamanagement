package eu.dzhw.fdz.metadatamanagement.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FdzProject.
 */
@Document(collection = "fdz_project")
public class FdzProject implements Serializable {
  private static final long serialVersionUID = 2466260798886385927L;

  @Id
  @NotNull
  @Field("name")
  @Pattern(regexp = "^[A-Za-z0-9äöüÄÖÜ][ A-Za-z0-9äöüÄÖÜ]*$")
  private String name;

  @Field("suf_doi")
  private String sufDoi;

  @Field("cuf_doi")
  private String cufDoi;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSufDoi() {
    return sufDoi;
  }

  public void setSufDoi(String sufDoi) {
    this.sufDoi = sufDoi;
  }

  public String getCufDoi() {
    return cufDoi;
  }

  public void setCufDoi(String cufDoi) {
    this.cufDoi = cufDoi;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    FdzProject fdzProject = (FdzProject) object;
    return Objects.equals(name, fdzProject.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public String toString() {
    return "FdzProject{name='" + name + "'" + ", sufDoi='" + sufDoi + "'"
        + ", cufDoi='" + cufDoi + "'" + '}';
  }
}
