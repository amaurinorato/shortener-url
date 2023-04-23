package com.example.vivy.urlshortener.router;

import com.example.vivy.urlshortener.handler.ShortenerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class ShortenerRouter {

  @Bean
  public RouterFunction<ServerResponse> route(ShortenerHandler shortenerHandler) {

    return RouterFunctions
            .route()
            .GET("/{urlId}", shortenerHandler::findLongUrlAndRedirect)
            .POST("/shorten", accept(MediaType.APPLICATION_JSON), shortenerHandler::shortenAndSave)
            .build();
  }
}