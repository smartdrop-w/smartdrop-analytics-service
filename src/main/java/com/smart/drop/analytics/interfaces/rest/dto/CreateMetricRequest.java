package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Solicitud para registrar una nueva métrica asociada a un reporte analítico")
public record CreateMetricRequest(
        @Schema(description = "Nombre canónico de la métrica física", example = "FLOW_RATE_MAX")
        @NotBlank(message = "name is required")
        String name,

        @Schema(description = "Valor numérico medido", example = "4.25")
        @NotNull(message = "value is required")
        BigDecimal value,

        @Schema(description = "Unidad de medida canónica (L/min, CELSIUS, PERCENTAGE)", example = "L/min")
        @NotBlank(message = "unit is required")
        String unit
) {
}
