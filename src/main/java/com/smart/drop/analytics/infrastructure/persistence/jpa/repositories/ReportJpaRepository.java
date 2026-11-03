package com.smart.drop.analytics.infrastructure.persistence.jpa.repositories;

import com.smart.drop.analytics.infrastructure.persistence.jpa.entities.ReportEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportJpaRepository extends JpaRepository<ReportEntity, Integer> {

    @EntityGraph(attributePaths = "metrics")
    List<ReportEntity> findByUserId(Integer userId);

    @EntityGraph(attributePaths = "metrics")
    List<ReportEntity> findTop10ByUserIdOrderByGeneratedAtDesc(Integer userId);

    long countByUserId(Integer userId);

    @Query("SELECT MAX(r.generatedAt) FROM ReportEntity r WHERE r.userId = :userId")
    LocalDateTime findLatestGeneratedAtByUserId(@Param("userId") Integer userId);
}
