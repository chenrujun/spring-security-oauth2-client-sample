package chenrujun.springbootoauth2clientsample.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class OboController {

    final WebClient webClient;

    public OboController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/obo/graph")
    @ResponseBody
    public String index(
        @RegisteredOAuth2AuthorizedClient("microsoft-graph") OAuth2AuthorizedClient oAuth2AuthorizedClient
    ) {
        String resourceUri = "https://graph.microsoft.com/v1.0/me/memberOf";
        return webClient
            .get()
            .uri(resourceUri)
            .attributes(oauth2AuthorizedClient(oAuth2AuthorizedClient))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}

