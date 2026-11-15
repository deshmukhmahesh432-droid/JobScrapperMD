package com.example.demo.service;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Job;
import com.example.demo.entity.JobDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.JobRepository;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Job saveJob(JobDTO dto) {

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Job title is required");
        }

        String source = dto.getSourceName() == null
                ? "MANUAL"
                : dto.getSourceName();

        String sourceId = dto.getSourceJobId() == null
                ? dto.getJobUrl()
                : dto.getSourceJobId();

        Job job = jobRepository
                .findBySourceNameAndSourceJobId(source, sourceId)
                .orElseGet(Job::new);

        job.setSourceName(source);
        job.setSourceJobId(sourceId);
        job.setTitle(dto.getTitle());
        job.setCompanyName(dto.getCompanyName());
        job.setLocation(dto.getLocation());
        job.setDescription(dto.getDescription());
        job.setExperienceMin(dto.getExperienceMin());
        job.setExperienceMax(dto.getExperienceMax());
        job.setEmploymentType(dto.getEmploymentType());
        job.setTechnology(dto.getTechnology());
        job.setPostedAt(dto.getPostedAt());
        job.setJobUrl(dto.getJobUrl());
        job.setLastVerifiedAt(Instant.now());
        job.setActive(true);

        return jobRepository.save(job);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        return jobRepository.findByActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException(
                        "Job not found with ID: " + id
                    ));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getFreshJobs() {

        Instant cutoff = Instant.now().minusSeconds(24 * 60 * 60);

        return jobRepository
                .findByActiveTrueAndPostedAtAfterOrderByPostedAtDesc(
                    cutoff
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> searchJobs(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return getAllJobs();
        }

        return jobRepository.searchJobs(keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getFresherJobs() {

        return jobRepository.findByActiveTrue()
                .stream()
                .filter(job ->
                    job.getExperienceMax() != null
                    && job.getExperienceMax() <= 1
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getExperiencedJobs() {

        return jobRepository.findByActiveTrue()
                .stream()
                .filter(job ->
                    job.getExperienceMin() != null
                    && job.getExperienceMin() >= 2
                )
                .toList();
    }

    @Override
    public void deleteJob(Long id) {

        Job job = getJobById(id);
        job.setActive(false);

        jobRepository.save(job);
    }

	
}
