package com.shortener.shortenermvc.config;

import com.shortener.shortenermvc.service.CodecInMemoryImpl;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.binder.MeterBinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservabilityConfig {

    @Bean
    public MeterBinder meterBinder() {
        return meterRegistry -> {

            Counter.builder("urls-counter")
                    .description("Number of write operations")
                    .register(meterRegistry);

            Gauge.builder("urls-gauge", () -> CodecInMemoryImpl.SHORT_TO_LONG_STORAGE.size())
                    .description("Size of underlying storage")
                    .register(meterRegistry);
        };
    }
}
