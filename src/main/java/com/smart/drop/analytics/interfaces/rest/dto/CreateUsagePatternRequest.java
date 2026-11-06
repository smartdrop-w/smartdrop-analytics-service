package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para registrar un nuevo patrón de uso hídrico detectado")
public record CreateUsagePatternRequest(
        @Schema(description = "Identificador del usuario", example = "2")
        @NotNull(message = "userId is required")
        Integer userId,

        @Schema(description = "Tipo de patrón (PEAK_HOURS, RECURRENT_LEAK, STABLE_BASE)", example = "PEAK_HOURS")
        @NotBlank(message = "type is required")
        String type,

        @Schema(description = "Título descriptivo del patrón", example = "Consumo Pico Matutino - Lavado")
        @NotBlank(message = "title is required")
        String title,

        @Schema(description = "Descripción detallada del comportamiento observado", example = "Se observa demanda superior a 15L/min entre 08:00 y 10:00 AM")
        String desc,

        @Schema(description = "Área o ubicación física asociada", example = "Línea de Envasado")
        String location,

        @Schema(description = "Nivel de impacto financiero u operativo (HIGH, MEDIUM, LOW)", example = "MEDIUM")
        String impact,

        @Schema(description = "Etiqueta clasificatoria", example = "OPTIMIZATION_TARGET")
        String tag,

        @Schema(description = "Franja horaria o intervalo temporal", example = "08:00 - 10:00")
        String time
) {
}
