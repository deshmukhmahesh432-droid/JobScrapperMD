package com.example.demo.service;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Job;
import com.example.demo.entity.JobDTO;
import com.example.demo.entity.ScraperLog;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.ScraperLogRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class JobScraperService {

    private final List<JobSource> jobSources;
    private final JobRepository jobRepository;
    private final ScraperLogRepository logRepository;

    private final DuplicateCheckService duplicateService;
    private final JobClassificationService classificationService;
    private final JobFreshnessService freshnessService;

    @Value("${scraper.keywords}")
    private String configuredKeywords;

    @Value("${scraper.max-keywords-per-source:20}")
    private int maxKeywords;

    public JobScraperService(
            List<JobSource> jobSources,
            JobRepository jobRepository,
            ScraperLogRepository logRepository,
            DuplicateCheckService duplicateService,
            JobClassificationService classificationService,
            JobFreshnessService freshnessService) {

        this.jobSources = jobSources;
        this.jobRepository = jobRepository;
        this.logRepository = logRepository;
        this.duplicateService = duplicateService;
        this.classificationService = classificationService;
        this.freshnessService = freshnessService;
    }

    public void collectJobs() {

    	System.out.println("========== SCRAPER STARTED ==========");
    	
        List<String> keywords = Arrays.stream(
                configuredKeywords.split(","))
            .map(String::trim)
            .filter(k -> !k.isBlank())
            .distinct()
            .limit(maxKeywords)
            .toList();

        for (JobSource source : jobSources) {

            for (String keyword : keywords) {

                scrapeSource(source, keyword);
            
                System.out.println("========== SCRAPER FINISHED ==========");

            }
        }
    }

    private void scrapeSource(
            JobSource source,
            String keyword) {

        ScraperLog log = new ScraperLog();

        log.setSourceName(source.getSourceName());
        log.setKeyword(keyword);
        log.setStartedAt(Instant.now());
        log.setStatus("RUNNING");

        int found = 0;
        int saved = 0;
        int duplicates = 0;
        int failed = 0;

        try {

            List<JobDTO> jobs = source.fetchJobs(keyword);

            if (jobs == null) {
                jobs = List.of();
            }

            found = jobs.size();

            for (JobDTO dto : jobs) {

                try {

                    if (dto.getSourceName() == null ||
                        dto.getSourceJobId() == null ||
                        dto.getTitle() == null ||
                        dto.getTitle().isBlank() ||
                        dto.getJobUrl() == null ||
                        dto.getJobUrl().isBlank()) {

                        failed++;
                        continue;
                    }

                    if (duplicateService.isDuplicate(dto)) {
                        duplicates++;
                        continue;
                    }

                    String level =
                        classificationService.classify(dto);

                    Job job = mapToEntity(dto, level);

                    jobRepository.saveAndFlush(job);

                    saved++;

                } catch (DataIntegrityViolationException e) {

                    // Another execution may have inserted the same job.
                    duplicates++;

                } catch (Exception e) {

                    failed++;

                    System.err.println(
                        "Job processing failed: " + e.getMessage()
                    );
                }
            }

            log.setStatus(failed > 0 ? "PARTIAL_SUCCESS" : "SUCCESS");

        } catch (Exception e) {

            log.setStatus("FAILED");
            log.setErrorMessage(e.getMessage());

            System.err.println(
                "Source failed: " + source.getSourceName()
                + " | " + keyword
                + " | " + e.getMessage()
            );

        } finally {

            log.setJobsFound(found);
            log.setJobsSaved(saved);
            log.setDuplicates(duplicates);
            log.setFailures(failed);
            log.setCompletedAt(Instant.now());

            logRepository.save(log);
        }
    }

    private Job mapToEntity(
            JobDTO dto,
            String experienceLevel) {

        Job job = new Job();

        job.setSourceName(dto.getSourceName());
        job.setSourceJobId(dto.getSourceJobId());

        job.setTitle(dto.getTitle());
        job.setCompanyName(dto.getCompanyName());
        job.setLocation(dto.getLocation());

        job.setDescription(dto.getDescription());

        job.setExperienceMin(dto.getExperienceMin());
        job.setExperienceMax(dto.getExperienceMax());

        job.setExperienceLevel(experienceLevel);

        job.setEmploymentType(dto.getEmploymentType());
        job.setTechnology(dto.getTechnology());

        job.setPostedAt(dto.getPostedAt());

        job.setFreshnessVerified(
            dto.isFreshnessVerified()
            && freshnessService.isFresh(dto)
        );

        job.setJobUrl(dto.getJobUrl());

        job.setFirstSeenAt(Instant.now());
        job.setLastVerifiedAt(Instant.now());
        job.setActive(true);

        return job;
    }
}
