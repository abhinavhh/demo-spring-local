package com.example.demo.Controllers;

import com.example.demo.Entities.Crops;
import com.example.demo.Repositories.CropRepository;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/crops")
public class CropController {
    private final CropRepository cropRepository;
    
    public CropController(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addCrop(@RequestBody Crops crop) {
        cropRepository.save(crop);
        return ResponseEntity.ok("Crop added successfully!");
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllCrops() {
        List<Map<String, Object>> cropsWithThresholds = cropRepository.findAll()
            .stream()
            .map(crop -> {
                Map<String, Object> cropMap = new HashMap<>();
                cropMap.put("id", crop.getId());
                cropMap.put("name", crop.getName());
                cropMap.put("minTemperature", crop.getMinTemperature());
                cropMap.put("maxTemperature", crop.getMaxTemperature());
                cropMap.put("minHumidity", crop.getMinHumidity());
                cropMap.put("maxHumidity", crop.getMaxHumidity());
                cropMap.put("minSoilMoisture", crop.getMinSoilMoisture());
                cropMap.put("maxSoilMoisture", crop.getMaxSoilMoisture());
                cropMap.put("irrigationStartTime", crop.getIrrigationStartTime()); // Include irrigationStartTime
                cropMap.put("irrigationEndTime", crop.getIrrigationEndTime()); // Include irrigationEndTime
                return cropMap;
            })
            .toList();
        return ResponseEntity.ok(cropsWithThresholds);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCropById(@PathVariable Long id) {
        return cropRepository.findById(id)
            .map(crop -> {
                Map<String, Object> cropMap = new HashMap<>();
                cropMap.put("id", crop.getId());
                cropMap.put("name", crop.getName());
                cropMap.put("minTemperature", crop.getMinTemperature());
                cropMap.put("maxTemperature", crop.getMaxTemperature());
                cropMap.put("minHumidity", crop.getMinHumidity());
                cropMap.put("maxHumidity", crop.getMaxHumidity());
                cropMap.put("minSoilMoisture", crop.getMinSoilMoisture());
                cropMap.put("maxSoilMoisture", crop.getMaxSoilMoisture());
                cropMap.put("irrigationStartTime", crop.getIrrigationStartTime()); // Include irrigationStartTime
                cropMap.put("irrigationEndTime", crop.getIrrigationEndTime()); // Include irrigationEndTime
                return cropMap;
            })
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/set-irrigation-time")
    public ResponseEntity<String> setIrrigationTime(@PathVariable Long id, @RequestBody Map<String, String> irrigationTime) {
        String startTime = irrigationTime.get("startTime");
        String endTime = irrigationTime.get("endTime");

        // Validate input
        if (startTime == null || endTime == null) {
            return ResponseEntity.badRequest().body("Start time and end time are required.");
        }

        try {
            LocalTime.parse(startTime); // Validate startTime format
            LocalTime.parse(endTime); // Validate endTime format
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid time format. Please use HH:mm.");
        }

        Crops crop = cropRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Crop not found"));

        crop.setIrrigationStartTime(LocalTime.parse(startTime));
        crop.setIrrigationEndTime(LocalTime.parse(endTime));

        cropRepository.save(crop);

        return ResponseEntity.ok("Irrigation time set successfully!");
    }
}