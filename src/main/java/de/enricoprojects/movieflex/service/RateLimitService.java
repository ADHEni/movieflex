package de.enricoprojects.movieflex.service;

import de.enricoprojects.movieflex.exception.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    public enum RateLimitAction {
        LOGIN,
        REGISTER,
        REFRESH,
        LOGOUT
    }
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public void checkRateLimit(RateLimitAction action, String clientIp) {
        String key = action + ":" + clientIp;

        Bucket bucket = buckets.computeIfAbsent(key, this::createNewBucket);

        if (!bucket.tryConsume(1)) {
            throw new RateLimitExceededException("Too many requests. Please try again later.");
        }
    }

    private Bucket createNewBucket(String key) {
        Bandwidth limit = Bandwidth.classic(
                10,
                Refill.greedy(10, Duration.ofMinutes(1))
        );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}