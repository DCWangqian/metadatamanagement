package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
@RequiredArgsConstructor
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
    implements LogoutSuccessHandler {

  public static final String BEARER_AUTHENTICATION = "Bearer ";

  private final TokenStore tokenStore;

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    // Request the token
    String token = request.getHeader("authorization");
    if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
      final OAuth2AccessToken oAuth2AccessToken =
          tokenStore.readAccessToken(StringUtils.substringAfter(token, BEARER_AUTHENTICATION));

      if (oAuth2AccessToken != null) {
        tokenStore.removeAccessToken(oAuth2AccessToken);
      }
    }

    response.setStatus(HttpServletResponse.SC_OK);
  }
}
