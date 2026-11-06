package com.smart.drop.analytics.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para actualizar el estado de atención de un patrón de uso")
public record UpdateUsagePatternStatusRequest(
        @Schema(description = "Nuevo estado del patrón (RESOLVED, DISMISSED, INVESTIGATING)", example = "RESOLVED")
        @NotBlank(message = "status is required")
        String status
) {
}
