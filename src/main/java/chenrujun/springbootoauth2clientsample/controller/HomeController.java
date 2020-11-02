package chenrujun.springbootoauth2clientsample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public HomeController(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @GetMapping({ "/", "/home" })
    @ResponseBody
    String home(OAuth2AuthenticationToken authentication) {
        LOGGER.info("home()");
        final OAuth2AuthorizedClient authorizedClient =
            this.oAuth2AuthorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        logAuthorizedClient(authorizedClient);
        return "home";
    }

    @GetMapping("/graph")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_https://graph.microsoft.com/User.Read')")
    String graph(OAuth2AuthenticationToken authentication) {
        LOGGER.info("graph()");
        final OAuth2AuthorizedClient authorizedClient =
            this.oAuth2AuthorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        // accessToken in authorizedClient can not refresh automatically.
        logAuthorizedClient(authorizedClient);
        return "graph";
    }

    @GetMapping("/office365")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_https://manage.office.com/ActivityFeed.Read')")
    String office365(OAuth2AuthenticationToken authentication) {
        LOGGER.info("office365()");
        final OAuth2AuthorizedClient authorizedClient =
            this.oAuth2AuthorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        logAuthorizedClient(authorizedClient);
        return "office365";
    }

    @GetMapping("/notExist")
    @ResponseBody
    @PreAuthorize("hasAuthority('SCOPE_notExist')")
    String notExist(OAuth2AuthenticationToken authentication) {
        LOGGER.info("notExist()");
        final OAuth2AuthorizedClient authorizedClient =
            this.oAuth2AuthorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());
        logAuthorizedClient(authorizedClient);
        return "notExist";
    }

    static void logAuthorizedClient(OAuth2AuthorizedClient authorizedClient) {
        LOGGER.info(
            "principalName = {}",
            authorizedClient.getPrincipalName()
        );
        LOGGER.info(
            "clientName = {}",
            Optional.of(authorizedClient)
                    .map(OAuth2AuthorizedClient::getClientRegistration)
                    .map(ClientRegistration::getClientName)
                    .orElse(null)
        );
        LOGGER.info(
            "accessTokenIssuedAt = {}",
            Optional.of(authorizedClient)
                    .map(OAuth2AuthorizedClient::getAccessToken)
                    .map(AbstractOAuth2Token::getIssuedAt)
                    .orElse(null)
        );
        LOGGER.info(
            "accessTokenExpiresAt = {}",
            Optional.of(authorizedClient)
                    .map(OAuth2AuthorizedClient::getAccessToken)
                    .map(AbstractOAuth2Token::getExpiresAt)
                    .orElse(null)
        );
        LOGGER.info(
            "refreshTokenIssuedAt = {}",
            Optional.of(authorizedClient)
                    .map(OAuth2AuthorizedClient::getRefreshToken)
                    .map(AbstractOAuth2Token::getIssuedAt)
                    .orElse(null)
        );
        LOGGER.info(
            "refreshTokenExpiresAt = {}",
            Optional.of(authorizedClient)
                    .map(OAuth2AuthorizedClient::getRefreshToken)
                    .map(AbstractOAuth2Token::getExpiresAt)
                    .orElse(null)
        );
    }

}
