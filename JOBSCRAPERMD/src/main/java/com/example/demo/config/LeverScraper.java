package com.example.demo.config;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.demo.entity.JobDTO;
import com.example.demo.service.JobSource;

import java.time.Instant;
import java.util.*;

@Component
public class LeverScraper implements JobSource {

    private final RestClient client;

    @Value("${scraper.lever.companies:}")
    private String companies;

    public LeverScraper(RestClient.Builder builder) {
        this.client = builder.build();
    }

    @Override
    public String getSourceName() {
        return "LEVER";
    }

    @Override
    public List<JobDTO> fetchJobs(String keyword) {

        List<JobDTO> result = new ArrayList<>();

        if (companies == null || companies.isBlank()) {
            return result;
        }

        for (String company : companies.split(",")) {

            try {

                List<Map<String, Object>> jobs = client.get()
                    .uri(
                        "https://api.lever.co/v0/postings/{company}?mode=json",
                        company.trim()
                    )
                    .retrieve()
                    .body(List.class);

                if (jobs == null) continue;

                for (Map<String, Object> item : jobs) {

                    String title = String.valueOf(item.get("text"));

                    String description = String.valueOf(
                        item.getOrDefault("descriptionPlain", "")
                    );

                    if (!matches(title + " " + description, keyword)) {
                        continue;
                    }

                    JobDTO dto = new JobDTO();

                    dto.setSourceName(getSourceName());
                    dto.setSourceJobId(String.valueOf(item.get("id")));
                    dto.setTitle(title);
                    dto.setCompanyName(company.trim());
                    dto.setDescription(description);
                    dto.setJobUrl((String) item.get("hostedUrl"));

                    Map categories = (Map) item.get("categories");

                    if (categories != null) {
                        dto.setLocation(
                            (String) categories.get("location")
                        );

                        dto.setEmploymentType(
                            (String) categories.get("commitment")
                        );
                    }

                    Object createdAt = item.get("createdAt");

                    if (createdAt instanceof Number) {
                        dto.setPostedAt(
                            Instant.ofEpochMilli(
                                ((Number) createdAt).longValue()
                            )
                        );
                        dto.setFreshnessVerified(true);
                    }

                    result.add(dto);
                }

            } catch (Exception e) {
                System.err.println(
                    "Lever error: " + company + " " + e.getMessage()
                );
            }
        }

        return result;
    }

    private boolean matches(String text, String keyword) {
        return text.toLowerCase()
            .contains(keyword.toLowerCase());
    }
}
