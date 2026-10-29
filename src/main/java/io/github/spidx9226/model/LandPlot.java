package io.github.spidx9226.model;

import java.math.BigDecimal;
import java.util.UUID;

public record LandPlot(
        UUID id,
        String cadastralNumber,
        String address,
        BigDecimal area,
        LandCategory category,
        LandPlotStatus status,
        UUID ownerId
) {}