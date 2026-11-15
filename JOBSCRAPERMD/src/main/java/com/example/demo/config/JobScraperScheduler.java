package com.example.demo.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.service.JobScraperService;

@Component
public class JobScraperScheduler {

    private final JobScraperService scraperService;

    @Value("${scraper.enabled:false}")
    private boolean enabled;

    public JobScraperScheduler(
            JobScraperService scraperService) {

        this.scraperService = scraperService;
    }

    @Scheduled(
        fixedDelayString = "${scraper.interval-ms:3600000}"
    )
    public void executeScraping() {

        if (!enabled) {
            return;
        }

        scraperService.collectJobs();
    }
}
