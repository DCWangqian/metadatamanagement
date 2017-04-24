package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the validation of the statistics of a variable. It checks the first quartile has a
 * numeric string, if the variable has a numeric data type.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {StatisticsFirstQuartileMustBeANumberOnNumericDataTypeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatisticsFirstQuartileMustBeANumberOnNumericDataType {
  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "statisticsFirstQuartileMustBeANumberOnNumericDataType.message}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
