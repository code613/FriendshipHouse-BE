package com.levdevs.freindshipbe.DTO;

import java.util.List;


public record LocationWithoutIdDTO( String name, List<SubLocationWithoutIdDTO> subLocations) {}
