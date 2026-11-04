package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para evaluar condiciones de alerta mediante el Patrón Strategy")
public record AlertEvaluationRequest(
        @Schema(description = "Tipo de líquido o dominio a evaluar ('WATER', 'BEER_WORT')", example = "WATER")
        @NotBlank(message = "liquidType is required")
        String liquidType,

        @Schema(description = "Valor físico medido por el sensor (cm/h o °C)", example = "7.8")
        @NotNull(message = "metricValue is required")
        Double metricValue,

        @Schema(description = "Umbral límite admisible antes de generar alerta", example = "5.0")
        @NotNull(message = "threshold is required")
        Double threshold,

        @Schema(description = "Hora del día en formato 24 horas (0 a 23)", example = "3")
        @NotNull(message = "hourOfDay is required")
        @Min(value = 0, message = "hourOfDay must be >= 0")
        @Max(value = 23, message = "hourOfDay must be <= 23")
        Integer hourOfDay
) {
}
