package com.smart.drop.analytics.interfaces.rest;

import com.smart.drop.analytics.application.queries.DashboardQueryService;
import com.smart.drop.analytics.application.queries.DashboardReadModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para visualización consolidada del panel analítico (CQRS Read View).
 */
@Tag(name = "Analytics Dashboard View", description = "Endpoints para consulta rápida y consolidada de métricas y reportes para pantallas de analítica")
@RestController
@RequestMapping("/api/v1/analytics/dashboard")
public class DashboardController {

    private final DashboardQueryService dashboardQueryService;

    public DashboardController(DashboardQueryService dashboardQueryService) {
        this.dashboardQueryService = dashboardQueryService;
    }

    @Operation(summary = "Obtener vista de dashboard por usuario", description = "Retorna el modelo consolidado con contadores, promedios y tarjetas recientes de métricas y reportes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard generado con éxito", content = @Content(schema = @Schema(implementation = DashboardReadModel.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<DashboardReadModel> getDashboardByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(dashboardQueryService.getDashboardByUserId(userId));
    }
}
