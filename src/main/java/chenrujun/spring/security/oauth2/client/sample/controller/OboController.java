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
public class OboController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OboController.class);
    final WebClient webClient;

    public OboController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/obo/graph")
    @ResponseBody
    public String oboGraph(
        @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient oAuth2AuthorizedClient
    ) {
        LOGGER.info("=================================== oboGraph()");
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

    @GetMapping("/obo/office")
    @ResponseBody
    public String oboOffice(
        @RegisteredOAuth2AuthorizedClient("office") OAuth2AuthorizedClient oAuth2AuthorizedClient
    ) {
        LOGGER.info("=================================== oboOffice()");
        HomeController.logAuthorizedClient(oAuth2AuthorizedClient);
        return "office";
    }

    @GetMapping("/obo/all")
    @ResponseBody
    public String oboAll(
        @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graphClient,
        @RegisteredOAuth2AuthorizedClient("office") OAuth2AuthorizedClient officeClient
    ) {
        LOGGER.info("=================================== oboAll()");
        HomeController.logAuthorizedClient(graphClient);
        HomeController.logAuthorizedClient(officeClient);
        return "all";
    }
}
