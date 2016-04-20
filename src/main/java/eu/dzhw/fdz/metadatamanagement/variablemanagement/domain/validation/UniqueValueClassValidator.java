package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;

/**
 * Ensure that value.valueClass is unique within the variable.
 * 
 * @author René Reitmann
 */
public class UniqueValueClassValidator
    implements ConstraintValidator<UniqueValueClass, List<ValidResponse>> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueValueClass constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(List<ValidResponse> validResponses, ConstraintValidatorContext context) {
    if (validResponses == null) {
      return true;
    }
    
    HashSet<String> classes = new HashSet<>();
    
    for (ValidResponse validResponse : validResponses) {
      if (validResponse.getValue() != null) {
        if (classes.contains(validResponse.getValue())) {
          return false;
        }
        classes.add(validResponse.getValue());
      }
    }
    
    return true;
  }
}
