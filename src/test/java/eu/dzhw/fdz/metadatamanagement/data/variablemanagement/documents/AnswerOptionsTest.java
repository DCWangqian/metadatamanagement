/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders.AnswerOptionBuilder;

/**
 * @author Daniel Katzberg
 *
 */
public class AnswerOptionsTest {
  
  @Test
  public void testHashCode() {
    
    // Arrange
    AnswerOption answerOption = new AnswerOptionBuilder().withCode(1).withLabel("1").build();

    // Act

    // Assert
    assertThat(answerOption.hashCode(), not(0));
  }

  @Test
  public void testEquals() {
    
    // Arrange
    AnswerOption answerOption = new AnswerOptionBuilder().build();
    AnswerOption answerOption2 = new AnswerOptionBuilder().build();

    // Act
    boolean checkSame = answerOption.equals(answerOption);
    boolean checkNull = answerOption.equals(null);
    boolean checkOtherClass = answerOption.equals(new Object());
    answerOption2.setCode(2);
    boolean checkCodeOther = answerOption.equals(answerOption2);
    answerOption.setCode(1);
    boolean checkCodeBoth = answerOption.equals(answerOption2);
    answerOption.setCode(2);
    boolean checkCodeBothSame = answerOption.equals(answerOption2);
    answerOption.setCode(null);
    answerOption2.setCode(null);
    answerOption2.setLabel("Label 2");
    boolean checkLabelOther = answerOption.equals(answerOption2);
    answerOption.setLabel("Label 1");
    boolean checkLabelBoth = answerOption.equals(answerOption2);
    answerOption.setLabel("Label 2");
    boolean checkLabelBothSame = answerOption.equals(answerOption2);

    // Assert
    assertEquals(true, checkSame);
    assertEquals(false, checkNull);
    assertEquals(false, checkOtherClass);
    assertEquals(false, checkCodeOther);
    assertEquals(false, checkCodeBoth);
    assertEquals(true, checkCodeBothSame);
    assertEquals(false, checkLabelOther);
    assertEquals(false, checkLabelBoth);
    assertEquals(true, checkLabelBothSame);
  }

}
