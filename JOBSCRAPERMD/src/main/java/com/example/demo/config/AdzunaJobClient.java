package com.example.demo.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.demo.entity.JobDTO;
import com.example.demo.service.JobSource;

import java.time.Instant;
import java.util.*;

@Component
public class AdzunaJobClient implements JobSource {

    private final RestClient client;

    @Value("${adzuna.app-id:}")
    private String appId;

    @Value("${adzuna.app-key:}")
    private String appKey;

    @Value("${adzuna.country:in}")
    private String country;

    public AdzunaJobClient(RestClient.Builder builder) {
        this.client = builder.build();
    }

    @Override
    public String getSourceName() {
        return "ADZUNA";
    }

    @Override
    public List<JobDTO> fetchJobs(String keyword) {

        List<JobDTO> result = new ArrayList<>();

        if (appId.isBlank() || appKey.isBlank()) {
            return result;
        }

        try {

            Map response = client.get()
                .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host("api.adzuna.com")
                    .path("/v1/api/jobs/{country}/search/1")
                    .queryParam("app_id", appId)
                    .queryParam("app_key", appKey)
                    .queryParam("results_per_page", 50)
                    .queryParam("what", keyword)
                    .queryParam("content-type", "application/json")
                    .build(country)
                )
                .retrieve()
                .body(Map.class);

            if (response == null) return result;

            List<Map<String, Object>> jobs =
                (List<Map<String, Object>>) response.get("results");

            if (jobs == null) return result;

            for (Map<String, Object> item : jobs) {

                JobDTO dto = new JobDTO();

                dto.setSourceName(getSourceName());
                dto.setSourceJobId(String.valueOf(item.get("id")));
                dto.setTitle((String) item.get("title"));
                dto.setJobUrl((String) item.get("redirect_url"));
                dto.setDescription((String) item.get("description"));

                Map company = (Map) item.get("company");
                Map location = (Map) item.get("location");

                if (company != null) {
                    dto.setCompanyName(
                        (String) company.get("display_name")
                    );
                }

                if (location != null) {
                    dto.setLocation(
                        (String) location.get("display_name")
                    );
                }

                Object created = item.get("created");

                if (created != null) {
                    dto.setPostedAt(
                        Instant.parse(created.toString())
                    );
                    dto.setFreshnessVerified(true);
                }

                result.add(dto);
            }

        } catch (Exception e) {
            System.err.println(
                "Adzuna error: " + e.getMessage()
            );
        }

        return result;
    }
}
