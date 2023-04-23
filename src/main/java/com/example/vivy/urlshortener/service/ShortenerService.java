package com.example.vivy.urlshortener.service;

import com.example.vivy.urlshortener.dto.UrlShortenerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShortenerService {

    @Value("${application.short-url-domain}")
    private String shortUrlDomain;

    Map<String, String> urls = new HashMap<>();

    public Mono<UrlShortenerDTO> findLongUrl(String encode) {
        return Mono.just(encode)
                .mapNotNull(x -> urls.get(encode))
                .map(UrlShortenerDTO::new)
                .switchIfEmpty(Mono.empty());
    }

    public Mono<UrlShortenerDTO> shortenAndSave(String url) {
        return encode(url)
                .map(x -> {
                    urls.put(x, url);
                    return new UrlShortenerDTO(shortUrlDomain + x);
                });
    }

    private Mono<String> encode(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return Mono.just(hexString.substring(0, 16));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash input", e);
        }
    }
}
