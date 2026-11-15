package com.example.demo.service;



import java.util.List;

import com.example.demo.entity.Job;
import com.example.demo.entity.JobDTO;

public interface JobService {

    Job saveJob(JobDTO jobDTO);

    List<Job> getAllJobs();

    Job getJobById(Long id);

    List<Job> getFreshJobs();

    List<Job> searchJobs(String keyword);

    List<Job> getFresherJobs();

    List<Job> getExperiencedJobs();

    void deleteJob(Long id);
}
