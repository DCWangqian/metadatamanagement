package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the validation of the statistics of a variable. It checks the third quartile has a
 * iso date string, if the variable has a date data type.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {StatisticsThirdQuartileMustBeAnIsoDateOnDateDataTypeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType {
  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "statisticsThirdQuartileMustBeAnIsoDateOnDateDataType}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
