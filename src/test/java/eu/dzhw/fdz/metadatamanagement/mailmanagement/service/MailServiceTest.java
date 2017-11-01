/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.mailmanagement.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.mail.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

/**
 * @author Daniel Katzberg
 *
 */
public class MailServiceTest extends AbstractTest {

  @Autowired
  private MailService mailService;

  private GreenMail greenMail;

  @Before
  public void before() {
    this.greenMail = new GreenMail(ServerSetupTest.SMTP);
    this.greenMail.start();
  }

  @After
  public void after() {
    this.greenMail.stop();
  }

  @Test
  public void testSendActivationEmail() throws Exception {
    // Arrange
    User user = User.builder().langKey("de")
      .email("anyMail@localhost.com")
      .build();

    // Act
    Future<Void> futureVoid = this.mailService.sendActivationEmail(user, "/");
    futureVoid.get();
    Message[] messages = greenMail.getReceivedMessages();

    // Arrange
    assertThat(futureVoid.isDone(), is(true));
    assertThat(messages.length, is(1));
  }

  @Test
  public void testSendPasswordResetMail() throws InterruptedException, ExecutionException {
    // Arrange
    User user = User.builder().langKey("de")
      .email("anyMail@localhost.com")
      .build();

    // Act
    Future<Void> futureVoid = this.mailService.sendPasswordResetMail(user, "/");
    futureVoid.get();
    Message[] messages = greenMail.getReceivedMessages();

    // Arrange
    assertThat(futureVoid.isDone(), is(true));
    assertThat(messages.length, is(1));
  }

}
