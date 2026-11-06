package com.codenova.eventregistration.controller;

import com.codenova.eventregistration.dto.RegistrationAdminResponse;
import com.codenova.eventregistration.service.AdminService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/registrations")
    public List<RegistrationAdminResponse> getAllRegistrations(
            @RequestParam(required = false) String search) {

        return adminService.getAllRegistrations(search);
    }
}
