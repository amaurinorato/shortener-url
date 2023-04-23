package com.example.vivy.urlshortener.handler;

import com.example.vivy.urlshortener.dto.UrlShortenerDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenerRouterTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void testGetNotFound() {
    webTestClient
      .get().uri("/abc")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody().json("{\"error\":\"not found\"}");
  }

  @Test
  void testPost() {
    postUrl()
      .expectStatus().isCreated()
      .expectBody(UrlShortenerDTO.class).value(x -> {
        assertThat(x.getUrl()).isEqualTo("http://localhost:8080/05046f26c83e8c88");
      });
  }

  @Test
  void testSuccessGet() {
    postUrl();
    webTestClient
      .get().uri("/05046f26c83e8c88")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectBody(UrlShortenerDTO.class).value(x -> {
        assertThat(x.getUrl()).isEqualTo("https://google.com");
      });
  }

  @Test
  void testSuccessGetAndRedirect() {
    postUrl();
    webTestClient
            .get().uri("/05046f26c83e8c88?redirect=true")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isPermanentRedirect();
  }

  private WebTestClient.ResponseSpec postUrl() {
    UrlShortenerDTO dto = new UrlShortenerDTO("https://google.com");
    return webTestClient
            .post().uri("/shorten")
            .body(Mono.just(dto), UrlShortenerDTO.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange();
  }
}