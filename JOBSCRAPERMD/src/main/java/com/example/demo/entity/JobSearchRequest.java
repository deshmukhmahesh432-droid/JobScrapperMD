package com.example.demo.entity;


public class JobSearchRequest {

    private String keyword;
    private String location;
    private String experienceLevel;
    private boolean last24Hours;

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public boolean isLast24Hours() { return last24Hours; }
    public void setLast24Hours(boolean last24Hours) {
        this.last24Hours = last24Hours;
    }
}
