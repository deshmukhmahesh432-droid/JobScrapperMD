package com.example.demo.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String companyName;

    private String website;

    private String careersUrl;

    private String sourceType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() { return website; }
    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCareersUrl() { return careersUrl; }
    public void setCareersUrl(String careersUrl) {
        this.careersUrl = careersUrl;
    }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
