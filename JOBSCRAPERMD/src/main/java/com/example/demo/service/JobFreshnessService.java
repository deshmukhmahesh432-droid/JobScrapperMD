package com.example.demo.service;


import com.example.demo.entity.JobDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JobFreshnessService {

    @Value("${scraper.freshness-hours:24}")
    private long freshnessHours;

    public boolean isFresh(JobDTO dto) {

        if (!dto.isFreshnessVerified()) {
            return false;
        }

        if (dto.getPostedAt() == null) {
            return false;
        }

        Instant now = Instant.now();

        Instant cutoff = now.minus(
            freshnessHours,
            ChronoUnit.HOURS
        );

        return !dto.getPostedAt().isBefore(cutoff)
                && !dto.getPostedAt().isAfter(now);
    }
}
