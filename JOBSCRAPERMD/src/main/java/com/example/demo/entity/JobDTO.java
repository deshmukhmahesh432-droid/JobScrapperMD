package com.example.demo.entity;



import java.time.Instant;

public class JobDTO {

    private String sourceName;
    private String sourceJobId;

    private String title;
    private String companyName;
    private String location;
    private String description;

    private Integer experienceMin;
    private Integer experienceMax;

    private String employmentType;
    private String technology;

    private Instant postedAt;
    private boolean freshnessVerified;

    private String jobUrl;

    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceJobId() { return sourceJobId; }
    public void setSourceJobId(String sourceJobId) {
        this.sourceJobId = sourceJobId;
    }

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

    public String getJobUrl() { return jobUrl; }
    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }
}
