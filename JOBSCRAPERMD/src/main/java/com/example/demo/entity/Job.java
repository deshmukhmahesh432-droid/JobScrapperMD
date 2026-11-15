package com.example.demo.entity;



import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "jobs",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_source_job",
        columnNames = {"source_name", "source_job_id"}
    ),
    indexes = {
        @Index(name = "idx_job_posted", columnList = "posted_at"),
        @Index(name = "idx_job_active", columnList = "active"),
        @Index(name = "idx_job_level", columnList = "experience_level")
    }
)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String companyName;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer experienceMin;
    private Integer experienceMax;

    private String experienceLevel;

    private String employmentType;
    private String technology;

    private Instant postedAt;

    private boolean freshnessVerified;

    private Instant firstSeenAt;
    private Instant lastVerifiedAt;

    private String sourceName;
    private String sourceJobId;

    @Column(length = 2048)
    private String jobUrl;

    private boolean active = true;

    @PrePersist
    public void onCreate() {
        if (firstSeenAt == null) {
            firstSeenAt = Instant.now();
        }
    }

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExperienceMin() { return experienceMin; }
    public void setExperienceMin(Integer experienceMin) {
        this.experienceMin = experienceMin;
    }

    public Integer getExperienceMax() { return experienceMax; }
    public void setExperienceMax(Integer experienceMax) {
        this.experienceMax = experienceMax;
    }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getTechnology() { return technology; }
    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public Instant getPostedAt() { return postedAt; }
    public void setPostedAt(Instant postedAt) {
        this.postedAt = postedAt;
    }

    public boolean isFreshnessVerified() {
        return freshnessVerified;
    }

    public void setFreshnessVerified(boolean freshnessVerified) {
        this.freshnessVerified = freshnessVerified;
    }

    public Instant getFirstSeenAt() { return firstSeenAt; }
    public void setFirstSeenAt(Instant firstSeenAt) {
        this.firstSeenAt = firstSeenAt;
    }

    public Instant getLastVerifiedAt() { return lastVerifiedAt; }
    public void setLastVerifiedAt(Instant lastVerifiedAt) {
        this.lastVerifiedAt = lastVerifiedAt;
    }

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceJobId() { return sourceJobId; }
    public void setSourceJobId(String sourceJobId) {
        this.sourceJobId = sourceJobId;
    }

    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) {
        this.active = active;
    }
}
