package com.example.vivy.urlshortener.dto;

public class UrlShortenerDTO {

    private String url;

    public UrlShortenerDTO() {

    }

    public UrlShortenerDTO(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
