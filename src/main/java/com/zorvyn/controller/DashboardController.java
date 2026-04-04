package com.zorvyn.controller;

import com.zorvyn.dto.response.DashboardSummaryDto;
import com.zorvyn.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }
    @GetMapping("/")
    public ResponseEntity<DashboardSummaryDto> getDashboard(){
        DashboardSummaryDto dashboardSummaryDto=dashboardService.getSummary();
        return new ResponseEntity<>(dashboardSummaryDto, HttpStatus.OK);
    }
}
