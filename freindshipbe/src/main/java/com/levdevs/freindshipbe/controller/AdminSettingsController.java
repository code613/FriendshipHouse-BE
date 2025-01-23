package com.levdevs.freindshipbe.controller;


import com.levdevs.freindshipbe.DTO.AdminSettingsDTO;
import com.levdevs.freindshipbe.Service.AdminSettingsService;
import com.levdevs.freindshipbe.enums.AdminSettingKey;
import com.levdevs.freindshipbe.enums.AdminSettingKey;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-settings")
public class AdminSettingsController {

    private final AdminSettingsService adminSettingsService;

    // Constructor injection
    public AdminSettingsController(AdminSettingsService adminSettingsService) {
        this.adminSettingsService = adminSettingsService;
    }

    // Get the setting value by key (e.g., "GUEST_STAY_LIMIT")
    @GetMapping("/{key}")
    public AdminSettingsDTO getSettingValue(@PathVariable AdminSettingKey key) {
        return adminSettingsService.getSettingValue(key);
    }

    // Update the setting value by key
    @PutMapping("/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // No content is returned on successful update
    public void updateSetting(@PathVariable AdminSettingKey key, @RequestBody String value) {
        adminSettingsService.updateSetting(key, value);
    }
}

