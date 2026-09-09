package com.shorten_url.controllers;

import com.shorten_url.models.UrlDto;
import com.shorten_url.services.ShortenUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api")
public class ShortenUrlController {

    @Autowired
    private ShortenUrlService shortenUrlService;

    private static final Logger logger = LoggerFactory.getLogger(ShortenUrlController.class);

    @PostMapping("/url")
    public ResponseEntity createShortenUrl(@RequestBody UrlDto rawUrlData) {
        var data = shortenUrlService.createShortenUrl(rawUrlData);
        return new ResponseEntity(data, HttpStatus.OK);
    }

    @GetMapping("{shortenValue}")
    public RedirectView GetShortenUrl(@PathVariable String shortenValue) {
        var data = shortenUrlService.getShortenUrl(shortenValue);
        return new RedirectView(data);
    }
}
