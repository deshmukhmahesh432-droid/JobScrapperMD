package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "saved_jobs",
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "job_id"}
    )
)
public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    private Instant savedAt = Instant.now();

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public Instant getSavedAt() { return savedAt; }
}
