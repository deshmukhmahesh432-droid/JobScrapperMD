package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Job;

import java.util.List;

@Service
public class JobFilterService {

    public List<Job> filterByLocation(
            List<Job> jobs, String location) {

        if (location == null || location.isBlank()) {
            return jobs;
        }

        return jobs.stream()
                .filter(job ->
                    job.getLocation() != null &&
                    job.getLocation().toLowerCase()
                        .contains(location.toLowerCase())
                )
                .toList();
    }

    public List<Job> filterByTechnology(
            List<Job> jobs, String technology) {

        if (technology == null || technology.isBlank()) {
            return jobs;
        }

        return jobs.stream()
                .filter(job ->
                    job.getTechnology() != null &&
                    job.getTechnology().toLowerCase()
                        .contains(technology.toLowerCase())
                )
                .toList();
    }
}
