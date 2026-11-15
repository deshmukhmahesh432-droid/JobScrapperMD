package com.example.demo.service;


import org.springframework.stereotype.Service;

import com.example.demo.entity.JobDTO;
import com.example.demo.repository.JobRepository;

@Service
public class DuplicateCheckService {

    private final JobRepository jobRepository;

    public DuplicateCheckService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public boolean isDuplicate(JobDTO dto) {

        if (dto.getSourceName() == null ||
            dto.getSourceJobId() == null ||
            dto.getSourceJobId().isBlank()) {
            return false;
        }

        return jobRepository.existsBySourceNameAndSourceJobId(
        	    dto.getSourceName(),
        	    dto.getSourceJobId()
        	);
    }
}
