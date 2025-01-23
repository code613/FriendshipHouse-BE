package com.levdevs.freindshipbe.Service;

import com.levdevs.freindshipbe.DAO.AdminSettingsRepository;
import com.levdevs.freindshipbe.DTO.AdminSettingsDTO;
import com.levdevs.freindshipbe.Entity.AdminSettings;
import com.levdevs.freindshipbe.enums.AdminSettingKey;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminSettingsService {
    @Autowired
    private AdminSettingsRepository adminSettingsRepository;

    // Get the setting value based on the key
    public AdminSettingsDTO getSettingValue(AdminSettingKey key) {

        // Retrieve the setting from the repository
        AdminSettings setting = adminSettingsRepository.findBySettingKey(key)
                .orElseThrow(() -> new IllegalArgumentException("Setting not found: " + key));

        // Convert the entity to DTO
        return new AdminSettingsDTO(setting.getSettingKey().toString(), setting.getSettingValue());
    }

    // Update the setting value based on the key, create if doesn't exist
    public void updateSetting(AdminSettingKey key, String value) {
        AdminSettings setting = adminSettingsRepository.findBySettingKey(key)
                .orElse(new AdminSettings(key, value)); // Create a new setting if not found
        setting.setSettingValue(value);
        adminSettingsRepository.save(setting); // Save the setting back to the DB
    }
}
