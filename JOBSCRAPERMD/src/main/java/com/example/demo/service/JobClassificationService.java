package com.example.demo.service;


import org.springframework.stereotype.Service;

import com.example.demo.entity.JobDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobClassificationService {

    private static final Pattern EXPERIENCE_PATTERN =
        Pattern.compile(
            "(\\d+)\\s*(?:-|to)\\s*(\\d+)\\s*(?:years?|yrs?)",
            Pattern.CASE_INSENSITIVE
        );

    private static final Pattern SINGLE_PATTERN =
        Pattern.compile(
            "(\\d+)\\s*\\+?\\s*(?:years?|yrs?)",
            Pattern.CASE_INSENSITIVE
        );

    public String classify(JobDTO dto) {

        String text = (
            value(dto.getTitle()) + " " +
            value(dto.getDescription())
        ).toLowerCase();

        Integer min = dto.getExperienceMin();
        Integer max = dto.getExperienceMax();

        if (min != null && max != null) {
            if (max <= 1) return "FRESHER";
            if (min >= 2) return "EXPERIENCED";
            return "MIXED";
        }

        if (min != null) {
            if (min >= 2) return "EXPERIENCED";
            if (min == 0) return "FRESHER";
        }

        Matcher range = EXPERIENCE_PATTERN.matcher(text);

        if (range.find()) {
            int minimum = Integer.parseInt(range.group(1));
            int maximum = Integer.parseInt(range.group(2));

            if (maximum <= 1) return "FRESHER";
            if (minimum >= 2) return "EXPERIENCED";
            return "MIXED";
        }

        Matcher single = SINGLE_PATTERN.matcher(text);

        if (single.find()) {
            int years = Integer.parseInt(single.group(1));

            if (years == 0 || years == 1) {
                return "FRESHER";
            }

            return "EXPERIENCED";
        }

        if (text.contains("intern") ||
            text.contains("graduate trainee") ||
            text.contains("entry level") ||
            text.contains("entry-level") ||
            text.contains("campus hiring") ||
            text.contains("trainee engineer")) {

            return "FRESHER";
        }

        return "UNKNOWN";
    }

    private String value(String input) {
        return input == null ? "" : input;
    }
}
