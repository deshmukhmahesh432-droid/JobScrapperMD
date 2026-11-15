package com.example.demo.controller;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Job;
import com.example.demo.service.JobSearchService;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class JobController {

    private final JobSearchService jobSearchService;

    public JobController(JobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    @GetMapping
    public Page<Job> getJobs(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String location,

            @RequestParam(required = false)
            String experienceLevel,

            @RequestParam(defaultValue = "false")
            boolean freshOnly,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size,

            @RequestParam(defaultValue = "postedAt")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                "Page cannot be negative"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                "Page size must be between 1 and 100"
            );
        }

        // Allow only approved database sorting fields.
        if (!java.util.Set.of(
                "postedAt",
                "firstSeenAt",
                "companyName",
                "title"
            ).contains(sortBy)) {

            throw new IllegalArgumentException(
                "Invalid sort field"
            );
        }

        Sort sort = direction.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(
            page,
            size,
            sort
        );

        return jobSearchService.search(
            keyword,
            location,
            experienceLevel,
            freshOnly,
            pageable
        );
    }
}
