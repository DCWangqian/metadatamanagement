package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Daniel Katzberg
 *
 */
public class DataTypeTest{
  
  @Test
  public void testValidNumericFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = DataTypes.getDataTypes().getNumericValueByLocale();
    assertEquals("numerisch", messageDE);
  }
  
  @Test
  public void testInValidNumericFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = DataTypes.getDataTypes().getNumericValueByLocale();
    assertThat("numerisch", not(messageEN));
  }
  
  public void testValidStringFieldGerman(){
    LocaleContextHolder.setLocale(Locale.GERMAN);
    String messageDE = DataTypes.getDataTypes().getStringValueByLocale();
    assertEquals("string", messageDE);
  }
  
  @Test
  public void testInValidStringFieldEnglish(){
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    String messageEN = DataTypes.getDataTypes().getStringValueByLocale();
    assertEquals("string", messageEN);
  }

}
