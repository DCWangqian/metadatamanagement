package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Value;

/**
 * Ensure that value.valueClass is unique within the variable.
 * 
 * @author René Reitmann
 */
public class UniqueValueClassValidator
    implements ConstraintValidator<UniqueValueClass, List<Value>> {

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
  public boolean isValid(List<Value> values, ConstraintValidatorContext context) {
    if (values == null) {
      return true;
    }
    
    HashSet<String> classes = new HashSet<>();
    
    for (Value value : values) {
      if (value.getValueClass() != null) {        
        if (classes.contains(value.getValueClass())) {
          return false;
        }
        classes.add(value.getValueClass());
      }
    }
    
    return true;
  }
}
