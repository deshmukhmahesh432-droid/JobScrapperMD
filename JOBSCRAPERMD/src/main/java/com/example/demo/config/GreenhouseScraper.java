
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.demo.entity.JobDTO;
import com.example.demo.service.JobSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GreenhouseScraper implements JobSource {

    private final RestClient client;

    @Value("${scraper.greenhouse.boards:}")
    private String boards;

    public GreenhouseScraper(RestClient.Builder builder) {
        this.client = builder.build();
    }

    @Override
    public String getSourceName() {
        return "GREENHOUSE";
    }

    @Override
    public List<JobDTO> fetchJobs(String keyword) {

        List<JobDTO> result = new ArrayList<>();

        if (boards == null || boards.isBlank()) {
            System.out.println("Greenhouse: No boards configured!");
            return result;
        }

        for (String board : boards.split(",")) {

            try {
                System.out.println("Greenhouse checking board: " + board);

                Map response = client.get()
                    .uri(
                        "https://boards-api.greenhouse.io/v1/boards/{board}/jobs?content=true",
                        board.trim()
                    )
                    .retrieve()
                    .body(Map.class);

                if (response == null) {
                    System.out.println("Greenhouse: Empty API response");
                    continue;
                }

                List<Map<String, Object>> jobs =
                    (List<Map<String, Object>>) response.get("jobs");
                
                System.out.println("GREENHOUSE BOARD: " + board);
                System.out.println("RAW JOB COUNT: " + jobs.size());

                for (Map<String, Object> item : jobs) {
                    System.out.println("GREENHOUSE TITLE: " + item.get("title"));
                }

                if (jobs == null) {
                    System.out.println("Greenhouse: Jobs array not found");
                    continue;
                }

                System.out.println(
                    "Greenhouse raw jobs received: " + jobs.size()
                );

                for (Map<String, Object> item : jobs) {

                    String title = String.valueOf(item.get("title"));

                    String description =
                        String.valueOf(item.getOrDefault("content", ""));

                    String searchText =
                        (title + " " + description).toLowerCase();

                    if (!searchText.contains(keyword.toLowerCase())) {
                        continue;
                    }

                    JobDTO dto = new JobDTO();

                    dto.setSourceName(getSourceName());
                    dto.setSourceJobId(String.valueOf(item.get("id")));
                    dto.setTitle(title);
                    dto.setCompanyName(board.trim());
                    dto.setDescription(description);
                    dto.setJobUrl((String) item.get("absolute_url"));

                    Map location = (Map) item.get("location");

                    if (location != null) {
                        dto.setLocation(
                            String.valueOf(location.get("name"))
                        );
                    }

                    dto.setPostedAt(null);
                    dto.setFreshnessVerified(false);

                    result.add(dto);
                }

                System.out.println(
                    "Greenhouse matching jobs for " + keyword +
                    ": " + result.size()
                );

            } catch (Exception e) {
                System.err.println(
                    "Greenhouse error for " + board + ": " + e.getMessage()
                );
                e.printStackTrace();
            }
        }

        return result;
    }
}
