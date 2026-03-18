//package com.shorten_url.repo;
//
//import com.shorten_url.entities.Url;
//import com.shorten_url.repositories.ShortenUrlRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
//import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ShortenUrlRepoTest {
//
//    @Autowired
//    private ShortenUrlRepository shortenUrlRepository;
//
//    @Test
//    public void findByShortUrl() {
//        try {
//            var test = new Url(1L, "abc123", "https://example.com");
//            Url saved = shortenUrlRepository.save(test);
//            Url found = shortenUrlRepository.findByShortUrl("abc123");
//            assertNotNull(found);
//            assertEquals(saved.getId(), found.getId());
//        } catch (Exception e) {
//            fail("Exception occurred: " + e.getMessage());
//        }
//    }
//}