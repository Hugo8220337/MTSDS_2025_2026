package com.domus.applications.utils;

import com.domus.applications.entities.*;
import com.domus.applications.repositories.*;
import com.domus.applications.valueObjects.*;

import java.time.LocalDate;

public class TestDataFactory {

    public static Competition insertCompetition(CompetitionRepository repo) {
        Competition c = new Competition(
                "Housing Competition 2024",
                ApplicationProcessType.M23,
                "2024/2025",
                new ApplicationPeriod(
                        LocalDate.parse("2024-01-01"),
                        LocalDate.parse("2024-12-31")
                )
        );
        return repo.save(c);
    }

    public static CompetitionPhase insertCompetitionPhase(CompetitionPhaseRepository repo, Competition competition) {
        CompetitionPhase p = new CompetitionPhase(
                competition,
                1,
                new ApplicationPeriod(
                        LocalDate.parse("2024-01-01"),
                        LocalDate.parse("2024-02-01")
                ),
                136
        );
        return repo.save(p);
    }

    public static CourseOption insertCourseOption(CourseOptionRepository repo, Application app) {
        CourseOption co = new CourseOption(app, 1L, 1);
        return repo.save(co);
    }
}