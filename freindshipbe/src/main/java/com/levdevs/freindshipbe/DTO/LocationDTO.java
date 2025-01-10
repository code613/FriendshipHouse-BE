package com.levdevs.freindshipbe.DTO;

import java.util.List;

public record LocationDTO(Long id, String name, List<SubLocationDTO> subLocations) {}
