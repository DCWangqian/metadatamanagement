package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterExpressionLanguages;

/**
 * Annotation for the validator for the filter expression language. Only values are allowes which
 * are defined in the {@link FilterExpressionLanguages}
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {ValidFilterExpressionLanguageValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFilterExpressionLanguage {
  
  /**
   * Defines the default error message.
   */
  public abstract String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validfilterexpressionlanguage.message";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
}
