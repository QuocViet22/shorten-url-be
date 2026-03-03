package com.shorten_url.controllers;

import com.shorten_url.models.UrlDto;
import com.shorten_url.services.ShortenUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/shorten-urls")
public class ShortenUrlController {

    @Autowired
    private ShortenUrlService shortenUrlService;

    private static final Logger logger = LoggerFactory.getLogger(ShortenUrlController.class);

    @PostMapping
    public ResponseEntity createShortenUrl(@RequestBody UrlDto rawUrlData) {
        try {
            var data = shortenUrlService.createShortenUrl(rawUrlData);
            return new ResponseEntity(data, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Create shorten url failed");
        }
    }

    @GetMapping("{shortenValue}")
    public RedirectView GetShortenUrl(@PathVariable String shortenValue) {
        try {
            var data = shortenUrlService.getShortenUrl(shortenValue);
            return new RedirectView(data);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found");
        }
    }
}
