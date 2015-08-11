package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.DataType;

/**
 * This annotation checks for an input of the datatype field. Only some values are acceptable,
 * define by the depending enum class {@link DataType}
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {DataTypeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDataType {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.data."
      + "variablemanagement.documents.enum.validation.validDataType.message}";

  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};


}
