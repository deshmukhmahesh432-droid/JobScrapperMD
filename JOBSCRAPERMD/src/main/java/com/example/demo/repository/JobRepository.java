package com.example.demo.repository;




import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByActiveTrue();

    List<Job> findByActiveTrueAndPostedAtAfterOrderByPostedAtDesc(
            Instant cutoff
    );

    Optional<Job> findBySourceNameAndSourceJobId(
            String sourceName,
            String sourceJobId
    );

    // For existing JobServiceImpl
    @Query("""
        SELECT j FROM Job j
        WHERE j.active = true
        AND (
            LOWER(COALESCE(j.title, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(j.companyName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(j.description, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(j.technology, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        """)
    List<Job> searchJobs(@Param("keyword") String keyword);
    
    boolean existsBySourceNameAndSourceJobId(
            String sourceName,
            String sourceJobId
    );


    // For JobSearchService with pagination and filters
 // For JobSearchService with pagination and filters
    @Query("""
        SELECT j FROM Job j
        WHERE j.active = true

        AND (
            :keyword = ''
            OR LOWER(COALESCE(j.title, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(j.companyName, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(j.description, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(COALESCE(j.technology, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )

        AND (
            :location = ''
            OR LOWER(COALESCE(j.location, '')) LIKE LOWER(CONCAT('%', :location, '%'))
        )

        AND (
            :experienceLevel = ''
            OR UPPER(COALESCE(j.experienceLevel, 'UNKNOWN')) = UPPER(:experienceLevel)
        )

        AND (
            :freshOnly = false
            OR (
                j.freshnessVerified = true
                AND j.postedAt IS NOT NULL
                AND j.postedAt BETWEEN :cutoff AND :now
            )
        )
        """)
    Page<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("experienceLevel") String experienceLevel,
            @Param("freshOnly") boolean freshOnly,
            @Param("cutoff") Instant cutoff,
            @Param("now") Instant now,
            Pageable pageable
    );
    }