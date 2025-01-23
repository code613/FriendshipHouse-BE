package com.levdevs.freindshipbe.DAO;

import com.levdevs.freindshipbe.Entity.AdminSettings;
import com.levdevs.freindshipbe.enums.AdminSettingKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminSettingsRepository extends JpaRepository<AdminSettings, Long> {
    // You can add custom queries here if needed, e.g. findById or findAll
 //   AdminSettings findBySettingKey(String settingKey);
    Optional<AdminSettings> findBySettingKey(AdminSettingKey key);

    boolean existsBySettingKey(AdminSettingKey settingKey);  // Use the enum as parameter type
}
