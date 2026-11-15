package com.example.demo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Job;
import com.example.demo.repository.JobRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
public class JobSearchService {

    private final JobRepository jobRepository;

    @Value("${scraper.freshness-hours:24}")
    private long freshnessHours;

    public JobSearchService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Page<Job> search(
            String keyword,
            String location,
            String experienceLevel,
            boolean freshOnly,
            Pageable pageable) {

        Instant now = Instant.now();

        Instant cutoff = now.minus(
            freshnessHours,
            ChronoUnit.HOURS
        );

        return jobRepository.searchJobs(
            normalize(keyword),
            normalize(location),
            normalize(experienceLevel),
            freshOnly,
            cutoff,
            now,
            pageable
        );
    }

    private String normalize(String value) {

        if (value == null || value.isBlank()) {
            return "";
        }

        return value.trim();
    }
}
