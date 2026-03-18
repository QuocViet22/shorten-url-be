package com.shorten_url.services;

import com.shorten_url.entities.Url;
import com.shorten_url.helpers.Base62Converter;
import com.shorten_url.helpers.SnowflakeIdGenerator;
import com.shorten_url.models.UrlDto;
import com.shorten_url.repositories.ShortenUrlRepository;
import com.shorten_url.services.impl.ShortenUrlImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortenUrlImplTest {

    @Mock
    private ShortenUrlRepository shortenUrlRepository;

    @Mock
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Mock
    private Base62Converter base62Converter;

    @InjectMocks
    private ShortenUrlImpl shortenUrlService;

    private final String SERVER_URL = "http://localhost:8080/";

    @BeforeEach
    void setUp() {
        // Inject @Value field manually
        ReflectionTestUtils.setField(shortenUrlService, "serverUrl", SERVER_URL);
    }

    @Test
    void createShortenUrl_shouldReturnShortenedUrl() {
        // Arrange
        long generatedId = 12345L;
        String base62 = "abc123";
        String longUrl = "https://example.com";

        UrlDto inputDto = new UrlDto(longUrl, null);
        Url savedEntity = new Url(generatedId, base62, longUrl);

        when(snowflakeIdGenerator.generateId()).thenReturn(generatedId);
        when(base62Converter.encode(generatedId)).thenReturn(base62);
        when(shortenUrlRepository.save(any(Url.class))).thenReturn(savedEntity);
//        given(shortenUrlRepository.findById(generatedId)).willReturn(Optional.of(savedEntity));
        doReturn(Optional.of(savedEntity))
                .when(shortenUrlRepository)
                .findById(generatedId);
        // Act
        UrlDto result = shortenUrlService.createShortenUrl(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(longUrl, result.longUrl);
        assertEquals(SERVER_URL + base62, result.shortUrl);

        verify(snowflakeIdGenerator).generateId();
        verify(base62Converter).encode(generatedId);
        verify(shortenUrlRepository).save(any(Url.class));
        verify(shortenUrlRepository).findById(generatedId);
    }

    @Test
    void createShortenUrl_shouldThrowException_whenUrlNotFound() {
        // Arrange
        long generatedId = 12345L;
        String base62 = "abc123";
        String longUrl = "https://example.com";

        UrlDto inputDto = new UrlDto(longUrl, null);

        when(snowflakeIdGenerator.generateId()).thenReturn(generatedId);
        when(base62Converter.encode(generatedId)).thenReturn(base62);
        when(shortenUrlRepository.findById(generatedId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> shortenUrlService.createShortenUrl(inputDto));

        assertEquals("URL not found", exception.getMessage());
    }

    @Test
    void getShortenUrl_shouldReturnLongUrl() {
        // Arrange
        String shortCode = "abc123";
        String longUrl = "https://example.com";

        Url entity = new Url(1L, shortCode, longUrl);

        when(shortenUrlRepository.findByShortUrl(shortCode)).thenReturn(entity);

        // Act
        String result = shortenUrlService.getShortenUrl(shortCode);

        // Assert
        assertEquals(longUrl, result);
        verify(shortenUrlRepository).findByShortUrl(shortCode);
    }
}
