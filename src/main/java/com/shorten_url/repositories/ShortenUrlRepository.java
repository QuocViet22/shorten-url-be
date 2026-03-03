package com.shorten_url.repositories;

import com.shorten_url.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortenUrlRepository extends JpaRepository<Url, Long> {
    Url findByShortUrl(String shortUrl);
}
