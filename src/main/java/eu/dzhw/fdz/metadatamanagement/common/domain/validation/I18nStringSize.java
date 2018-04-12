package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Annotation for i18n String (de/en Strings).
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {I18nStringSizeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nStringSize {
  
  /**
   * Defines the default error message.
   */
  String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "i18nstringsize.message";
  
  /**
   * This contains groups.
   */
  Class<?>[]groups() default {};
  
  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[]payload() default {};
  
  /**
   * Get the max length of the {@link I18nString}.
   * 
   * @return size the element must be equal or lower to.
   */
  int max() default Integer.MAX_VALUE;

  /**
   * Get the min length of the {@link I18nString}.
   * 
   * @return size the element must be equal or higher to.
   */
  int min() default 0;
}
