package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for i18n String (de/en Strings). It checks that both string are set. 
 * It is also okay, if no fields are set. If one language is set, the other languages 
 * have to be set.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {I18nStringEntireNotEmptyOptionalValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nStringEntireNotEmptyOptional {
  
  /**
   * Defines the default error message.
   */
  String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "i18nstringnotemptyoptional.message";
  
  /**
   * This contains groups.
   */
  Class<?>[]groups() default {};
  
  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[]payload() default {};
}
