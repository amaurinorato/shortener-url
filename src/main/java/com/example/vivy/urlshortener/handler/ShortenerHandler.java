package com.example.vivy.urlshortener.handler;

import com.example.vivy.urlshortener.dto.UrlShortenerDTO;
import com.example.vivy.urlshortener.service.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
public class ShortenerHandler {

    @Autowired
    ShortenerService shortenerService;

    public Mono<ServerResponse> findLongUrlAndRedirect(ServerRequest serverRequest) {
        boolean redirect = Boolean.parseBoolean(serverRequest.queryParam("redirect").orElse("false"));
        String shortUrl = serverRequest.pathVariable("urlId");

        return shortenerService.findLongUrl(shortUrl)
                .flatMap(longUrl -> {
                    if (redirect)
                        return permanentRedirect(URI.create(longUrl.getUrl())).build();
                    return createSuccessPage(HttpStatus.OK, longUrl);
                })
                .switchIfEmpty(createNotFoundPage());
    }

    private Mono<ServerResponse> createNotFoundPage() {
        return status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"error\":\"not found\"}");
    }

    private Mono<ServerResponse> createSuccessPage(HttpStatus status, UrlShortenerDTO url) {
        return status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(url);
    }

    public Mono<ServerResponse> shortenAndSave(ServerRequest serverRequest) {
        return
                serverRequest.body(BodyExtractors.toMono(UrlShortenerDTO.class))
                        .flatMap(x -> shortenerService.shortenAndSave(x.getUrl()))
                        .flatMap(urlInfo -> createSuccessPage(HttpStatus.CREATED, urlInfo));
    }
}
