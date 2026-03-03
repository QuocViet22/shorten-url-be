package com.shorten_url.services;

import com.shorten_url.models.UrlDto;

public interface ShortenUrlService {
    UrlDto createShortenUrl(UrlDto rawUrl);

    String getShortenUrl(String shortenValue);
}
