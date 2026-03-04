package com.shorten_url.services.impl;

import com.shorten_url.entities.Url;
import com.shorten_url.helpers.Base62Converter;
import com.shorten_url.helpers.SnowflakeIdGenerator;
import com.shorten_url.models.UrlDto;
import com.shorten_url.repositories.ShortenUrlRepository;
import com.shorten_url.services.ShortenUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShortenUrlImpl implements ShortenUrlService {

    @Autowired
    private ShortenUrlRepository shortenUrlRepository;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Autowired
    private Base62Converter base62Converter;

    @Value("spring.application.server-url")
    private String serverUrl;

    @Override
    public UrlDto createShortenUrl(UrlDto rawUrlData) {
        // Generate unique id
        var newId = snowflakeIdGenerator.generateId();
        var base62Id = base62Converter.encode(newId);
        // Create new record
        var newShortedUrl = new Url(newId, base62Id, rawUrlData.longUrl);
        shortenUrlRepository.save(newShortedUrl);
        var shortenUrl = shortenUrlRepository.findById(newId)
                        .orElseThrow(() -> new RuntimeException("URL not found"));
        var shortenUrlData = serverUrl + "/shorten-urls/" + shortenUrl.getShortUrl();
        return new UrlDto(shortenUrl.getLongUrl(), shortenUrlData);
    }

    @Override
    public String getShortenUrl(String shortenValue) {
        var data = shortenUrlRepository.findByShortUrl(shortenValue);
        return data.getLongUrl();
    }
}
