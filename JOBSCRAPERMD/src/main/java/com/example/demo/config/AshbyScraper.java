package com.example.demo.config;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.demo.entity.JobDTO;
import com.example.demo.service.JobSource;

import java.time.Instant;
import java.util.*;

@Component
public class AshbyScraper implements JobSource {

    private final RestClient client;

    @Value("${scraper.ashby.boards:}")
    private String boards;

    public AshbyScraper(RestClient.Builder builder) {
        this.client = builder.build();
    }

    @Override
    public String getSourceName() {
        return "ASHBY";
    }

    @Override
    public List<JobDTO> fetchJobs(String keyword) {

        List<JobDTO> result = new ArrayList<>();

        if (boards == null || boards.isBlank()) {
            return result;
        }

        for (String board : boards.split(",")) {

            try {

                Map response = client.get()
                    .uri(
                        "https://api.ashbyhq.com/posting-api/job-board/{board}?includeCompensation=false",
                        board.trim()
                    )
                    .retrieve()
                    .body(Map.class);

                if (response == null) continue;

                List<Map<String, Object>> jobs =
                    (List<Map<String, Object>>) response.get("jobs");

                if (jobs == null) continue;

                for (Map<String, Object> item : jobs) {

                    String title = String.valueOf(item.get("title"));

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
                    dto.setCompanyName(board.trim());
                    dto.setDescription(description);
                    dto.setJobUrl((String) item.get("jobUrl"));
                    dto.setLocation((String) item.get("location"));

                    Object publishedAt = item.get("publishedAt");

                    if (publishedAt != null) {
                        dto.setPostedAt(
                            Instant.parse(publishedAt.toString())
                        );
                        dto.setFreshnessVerified(true);
                    }

                    result.add(dto);
                }

            } catch (Exception e) {
                System.err.println(
                    "Ashby error: " + board + " " + e.getMessage()
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
