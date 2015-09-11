package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * The answer option represents the options of answer for closed questions. The answer option bases
 * on two elements: the answer code itself and a label.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders")
public class AnswerOption {

  public static final Field CODE_FIELD = new Field("code");
  public static final Field LABEL_FIELD = new Field("label");

  /**
   * This is the code of an answer. It is internal code representation of the answer.
   */
  @NotNull(groups = {Create.class, Edit.class})
  private Integer code;

  /**
   * The Label is the answer which are read by the subjects.
   */
  @Size(max = 60, groups = {Create.class, Edit.class})
  private String label;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("code", code).add("label", label).toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(code, label);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      AnswerOption that = (AnswerOption) object;
      return Objects.equal(this.code, that.code) && Objects.equal(this.label, that.label);
    }
    return false;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}
