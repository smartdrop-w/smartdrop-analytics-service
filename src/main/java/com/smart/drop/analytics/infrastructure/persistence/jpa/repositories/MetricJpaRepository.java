package com.smart.drop.analytics.infrastructure.persistence.jpa.repositories;

import com.smart.drop.analytics.infrastructure.persistence.jpa.entities.MetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetricJpaRepository extends JpaRepository<MetricEntity, Integer> {

    List<MetricEntity> findByUserId(Integer userId);

    List<MetricEntity> findByReport_ReportId(Integer reportId);

    List<MetricEntity> findTop10ByUserIdOrderByRecordedAtDesc(Integer userId);

    long countByUserId(Integer userId);

    @Query("SELECT AVG(m.value) FROM MetricEntity m WHERE m.userId = :userId")
    Double findAverageValueByUserId(@Param("userId") Integer userId);
}
