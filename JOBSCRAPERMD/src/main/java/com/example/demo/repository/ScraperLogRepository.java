package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ScraperLog;

import java.util.List;

public interface ScraperLogRepository
        extends JpaRepository<ScraperLog, Long> {

    List<ScraperLog>
        findTop100ByOrderByStartedAtDesc();

    List<ScraperLog>
        findBySourceNameOrderByStartedAtDesc(String sourceName);
}
