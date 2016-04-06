/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.logmanagement.aop;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import javax.annotation.PostConstruct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.mockito.Mockito;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.logmanagement.aop.LoggingAspect;

/**
 * @author Daniel Katzberg
 *
 */
public class LoggingAspectTest extends AbstractTest {

  private LoggingAspect loggingAspect;

  @PostConstruct
  public void postConstruct() {
    this.loggingAspect = new LoggingAspect();
  }

  @Test
  public void testLoggingPointcut() {
    // Arrange

    // Act
    this.loggingAspect.loggingPointcut(); // should start logAround

    // Assert
    assertThat(this.loggingAspect, not(nullValue()));
  }

  @Test
  public void testLogAfterThrowing() {
    // Arrange
    JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
    Signature signature = Mockito.mock(Signature.class);
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getDeclaringTypeName()).thenReturn("Declaring Type Name");
    when(signature.getName()).thenReturn("Name");
    Exception exception = Mockito.mock(Exception.class);
    when(exception.getCause()).thenReturn(Mockito.mock(Throwable.class));

    // Act
    this.loggingAspect.logAfterThrowing(joinPoint, exception);

    // Assert
    assertThat(this.loggingAspect, not(nullValue()));
  }
  
  @Test
  public void testLogAround() throws Throwable {
    // Arrange
    ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
    Signature signature = Mockito.mock(Signature.class);
    when(joinPoint.getSignature()).thenReturn(signature);
    when(signature.getDeclaringTypeName()).thenReturn("Declaring Type Name");
    when(signature.getName()).thenReturn("Name");

    // Act
    this.loggingAspect.logAround(joinPoint);

    // Assert
    assertThat(this.loggingAspect, not(nullValue()));
  }

}
