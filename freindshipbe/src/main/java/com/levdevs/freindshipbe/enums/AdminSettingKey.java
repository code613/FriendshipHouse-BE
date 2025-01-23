package com.levdevs.freindshipbe.enums;

public enum AdminSettingKey {
    MAX_STAY_TIME_IN_DAYS("Maximum stay time allowed in days");


    private final String description;

    AdminSettingKey(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

