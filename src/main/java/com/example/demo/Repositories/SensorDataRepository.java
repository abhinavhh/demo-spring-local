package com.example.demo.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.SensorData;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findByUserIdAndSensorTypeAndTimestampBetween(
        Long userId, String sensorType, LocalDateTime start, LocalDateTime end
    );

    List<SensorData> findByUserIdAndSensorTypeOrderByTimestampDesc(Long userId, String sensorType);
}
