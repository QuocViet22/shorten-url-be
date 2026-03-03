package com.shorten_url.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "url")
public class Url {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "long_url")
    private String longUrl;

    public Url(Long id, String shortUrl, String longUrl) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }
}
