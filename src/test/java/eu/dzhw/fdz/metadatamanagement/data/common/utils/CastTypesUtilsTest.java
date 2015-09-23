/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.common.utils;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class CastTypesUtilsTest {
  
  @Test
  public void testDefaultLongIntCast() {
    //Arrange
    CastTypesUtils castTypesUtils = new CastTypesUtils();
    
    //Act
    int positive = CastTypesUtils.castLongToInt(25);
    int negative = CastTypesUtils.castLongToInt(-25);
    
    //Assert
    assertThat(positive, is(25));
    assertThat(negative, is(-25));
    assertThat(castTypesUtils, is(notNullValue()));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testErrorLongIntCastMax() {
    //Arrange
    long overMax = Integer.MAX_VALUE;
    overMax += 100L;
    
    //Act    
    CastTypesUtils.castLongToInt(overMax);
    
    //Assert (expect IlligalArgumentException)
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testErrorLongIntCastMin() {
    //Arrange
    long underMin = Integer.MIN_VALUE;
    underMin -= 100L;
    
    //Act    
    CastTypesUtils.castLongToInt(underMin);
    
    //Assert (expect IlligalArgumentException)
  }

}
