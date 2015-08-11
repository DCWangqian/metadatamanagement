package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types.DataTypes;

/**
 * The validator checks for a not null scale level field, if a data type is numeric.
 * 
 * @author Daniel Katzberg
 *
 */
public class NotNullScaleLevelOnNumericDataTypeValidator
    implements ConstraintValidator<NotNullScaleLevelOnNumericDataType, VariableDocument> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(NotNullScaleLevelOnNumericDataType constraintAnnotation) {
    // Do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(VariableDocument value, ConstraintValidatorContext context) {

    // check for null
    if (value.getDataType() == null) {
      return true;
    }

    // check numeric field with look for a null scale level
    if (value.getDataType().equals(DataTypes.getDataTypes().getNumericValueByLocale())
        && value.getScaleLevel() == null) {
      return false;
    }

    // not a numeric field
    return true;
  }

}
