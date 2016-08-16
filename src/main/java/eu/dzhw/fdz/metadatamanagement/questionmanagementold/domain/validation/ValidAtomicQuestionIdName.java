package eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the validation of the id. It has to be by the pattern: 
 * DataAcquisitionProjectId-VariableName.
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {ValidAtomicQuestionIdNameValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAtomicQuestionIdName {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validatomicquestionidname.message}";
  
  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};
  
  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};
}