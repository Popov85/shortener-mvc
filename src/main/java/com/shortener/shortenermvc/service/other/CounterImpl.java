package com.shortener.shortenermvc.service.other;

import org.springframework.stereotype.Repository;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CounterImpl implements Counter {

    // Up to 2^63-1, an extremely huge number;
    private final AtomicLong counter = new AtomicLong(0L);

    @Override
    public String getNextId() {
        // Thread-safe increment
        Long nextInteger =
                counter.getAndIncrement();
        return String.valueOf(nextInteger);
    }
}
