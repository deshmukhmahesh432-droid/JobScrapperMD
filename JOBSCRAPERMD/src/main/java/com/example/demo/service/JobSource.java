package com.example.demo.service;


import java.util.List;

import com.example.demo.entity.JobDTO;



import java.util.List;

public interface JobSource {

    String getSourceName();

    List<JobDTO> fetchJobs(String keyword);
}

