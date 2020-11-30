package chenrujun.spring.security.oauth2.client.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class ClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);
    final WebClient webClient;

    public ClientController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/client/graph")
    @ResponseBody
    public String clientGraph(
        @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient oAuth2AuthorizedClient
    ) {
        LOGGER.info("=================================== clientGraph()");
        HomeController.logAuthorizedClient(oAuth2AuthorizedClient);
        String resourceUri = "https://graph.microsoft.com/v1.0/me/memberOf";
        return webClient
            .get()
            .uri(resourceUri)
            .attributes(oauth2AuthorizedClient(oAuth2AuthorizedClient))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/client/office")
    @ResponseBody
    public String clientOffice(
        @RegisteredOAuth2AuthorizedClient("office") OAuth2AuthorizedClient oAuth2AuthorizedClient
    ) {
        LOGGER.info("=================================== clientOffice()");
        HomeController.logAuthorizedClient(oAuth2AuthorizedClient);
        return "office";
    }

    @GetMapping("/client/arm")
    @ResponseBody
    public String clientArm(
        @RegisteredOAuth2AuthorizedClient("arm") OAuth2AuthorizedClient oAuth2AuthorizedClient
    ) {
        LOGGER.info("=================================== clientArm()");
        HomeController.logAuthorizedClient(oAuth2AuthorizedClient);
        return "arm";
    }

    @GetMapping("/client/all")
    @ResponseBody
    public String clientAll(
        @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graphClient,
        @RegisteredOAuth2AuthorizedClient("office") OAuth2AuthorizedClient officeClient,
        @RegisteredOAuth2AuthorizedClient("arm") OAuth2AuthorizedClient armClient
    ) {
        LOGGER.info("=================================== clientAll()");
        HomeController.logAuthorizedClient(graphClient);
        HomeController.logAuthorizedClient(officeClient);
        HomeController.logAuthorizedClient(armClient);
        return "all";
    }
}
