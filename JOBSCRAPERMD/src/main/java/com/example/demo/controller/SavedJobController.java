package com.example.demo.controller;




import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Job;
import com.example.demo.entity.SavedJob;
import com.example.demo.entity.User;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.SavedJobRepository;
import com.example.demo.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public SavedJobController(
            SavedJobRepository savedJobRepository,
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<String> saveJob(
            @PathVariable Long jobId,
            Principal principal) {

        User user = userRepository.findByEmail(
            principal.getName()
        ).orElseThrow();

        Job job = jobRepository.findById(jobId)
            .orElseThrow();

        if (savedJobRepository.existsByUserIdAndJobId(
                user.getId(), jobId)) {

            return ResponseEntity.ok("Job already saved");
        }

        SavedJob savedJob = new SavedJob();

        savedJob.setUser(user);
        savedJob.setJob(job);

        savedJobRepository.save(savedJob);

        return ResponseEntity.ok("Job saved successfully");
    }

    @GetMapping
    public List<SavedJob> getSavedJobs(Principal principal) {

        User user = userRepository.findByEmail(
            principal.getName()
        ).orElseThrow();

        return savedJobRepository.findByUserId(user.getId());
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> removeSavedJob(
            @PathVariable Long jobId,
            Principal principal) {

        User user = userRepository.findByEmail(
            principal.getName()
        ).orElseThrow();

        savedJobRepository.deleteByUserIdAndJobId(
            user.getId(), jobId
        );

        return ResponseEntity.ok("Saved job removed");
    }
}
