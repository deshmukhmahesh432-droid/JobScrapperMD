package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "scraper_logs")
public class ScraperLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceName;

    private String keyword;

    private int jobsFound;

    private int jobsSaved;

    private int duplicates;

    private int failures;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private Instant startedAt;

    private Instant completedAt;

    public Long getId() { return id; }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getJobsFound() { return jobsFound; }
    public void setJobsFound(int jobsFound) {
        this.jobsFound = jobsFound;
    }

    public int getJobsSaved() { return jobsSaved; }
    public void setJobsSaved(int jobsSaved) {
        this.jobsSaved = jobsSaved;
    }

    public int getDuplicates() { return duplicates; }
    public void setDuplicates(int duplicates) {
        this.duplicates = duplicates;
    }

    public int getFailures() { return failures; }
    public void setFailures(int failures) {
        this.failures = failures;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}
