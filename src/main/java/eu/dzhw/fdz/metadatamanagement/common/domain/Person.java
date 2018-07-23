package eu.dzhw.fdz.metadatamanagement.common.domain;

import javax.validation.constraints.NotEmpty;

import org.javers.core.metamodel.annotation.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A representation of a person.
 *
 * @author Daniel Katzberg
 *
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class Person {

  @NotEmpty(message = "global.error.person.first-name.not-empty")
  private String firstName;

  private String middleName;

  @NotEmpty(message = "global.error.person.last-name.not-empty")
  private String lastName;
}
