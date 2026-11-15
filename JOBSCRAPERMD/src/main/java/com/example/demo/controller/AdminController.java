package com.example.demo.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.ScraperLog;
import com.example.demo.repository.ScraperLogRepository;
import com.example.demo.service.JobScraperService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final JobScraperService scraperService;
    private final ScraperLogRepository logRepository;

    public AdminController(
            JobScraperService scraperService,
            ScraperLogRepository logRepository) {

        this.scraperService = scraperService;
        this.logRepository = logRepository;
    }

    @PostMapping("/scrape")
    public ResponseEntity<Map<String, String>> runScraper() {

        scraperService.collectJobs();

        return ResponseEntity.ok(
            Map.of(
                "status", "COMPLETED",
                "message", "Scraper execution finished"
            )
        );
    }

    @GetMapping("/scraper-logs")
    public List<ScraperLog> getScraperLogs() {

        return logRepository.findTop100ByOrderByStartedAtDesc();
    }

    @GetMapping("/scraper-logs/{source}")
    public List<ScraperLog> getLogsBySource(
            @PathVariable String source) {

        return logRepository
            .findBySourceNameOrderByStartedAtDesc(
                source.toUpperCase()
            );
    }
}
