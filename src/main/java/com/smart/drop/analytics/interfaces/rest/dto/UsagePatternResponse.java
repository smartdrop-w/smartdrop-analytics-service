package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle del patrón de uso detectado y su etiqueta clasificatoria")
public record UsagePatternResponse(
        @Schema(description = "Identificador único del patrón", example = "1")
        Integer patternId,

        @Schema(description = "Identificador del usuario", example = "2")
        Integer userId,

        @Schema(description = "Tipo de patrón", example = "PEAK_HOURS")
        String type,

        @Schema(description = "Título descriptivo", example = "Consumo Pico Matutino - Lavado")
        String title,

        @Schema(description = "Descripción del patrón", example = "Demanda de 15L/min en horario matutino")
        String desc,

        @Schema(description = "Ubicación", example = "Línea de Envasado")
        String location,

        @Schema(description = "Nivel de impacto", example = "MEDIUM")
        String impact,

        @Schema(description = "Etiqueta o estado de gestión (RESOLVED, ACTIVE, INVESTIGATING)", example = "ACTIVE")
        String tag,

        @Schema(description = "Horario del patrón", example = "08:00 - 10:00")
        String time
) {
}
