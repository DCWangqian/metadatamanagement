/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.CustomParameterizedException;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ParameterizedErrorDto;

/**
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class CustomParameterizedExceptionTest {

  @Test
  public void testCustomParameterizedException() {
    // Arrange
    CustomParameterizedException exception =
        new CustomParameterizedException("message", "param", "param2");

    // Act
    ParameterizedErrorDto dto = exception.getErrorDto();

    // Assert
    assertThat(exception, not(nullValue()));
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("message"));
    assertThat(dto.getParams().length, is(2));
    assertThat(dto.getParams()[0], is("param"));
    assertThat(dto.getParams()[1], is("param2"));
  }
}
