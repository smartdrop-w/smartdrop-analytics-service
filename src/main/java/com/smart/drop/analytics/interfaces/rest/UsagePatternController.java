package com.smart.drop.analytics.interfaces.rest;

import com.smart.drop.analytics.application.services.UsagePatternService;
import com.smart.drop.analytics.domain.model.entities.UsagePattern;
import com.smart.drop.analytics.interfaces.rest.dto.CreateUsagePatternRequest;
import com.smart.drop.analytics.interfaces.rest.dto.UpdateUsagePatternStatusRequest;
import com.smart.drop.analytics.interfaces.rest.dto.UsagePatternResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para detección y gestión de patrones de consumo hídrico.
 */
@Tag(name = "Usage Patterns Detection", description = "Endpoints para registro y seguimiento de patrones de consumo anómalos o de optimización")
@RestController
@RequestMapping("/api/v1/analytics/patterns")
public class UsagePatternController {

    private final UsagePatternService usagePatternService;

    public UsagePatternController(UsagePatternService usagePatternService) {
        this.usagePatternService = usagePatternService;
    }

    @Operation(summary = "Registrar nuevo patrón de uso", description = "Crea un registro de patrón detectado indicando impacto, franja horaria y ubicación.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Patrón registrado exitosamente", content = @Content(schema = @Schema(implementation = UsagePatternResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<UsagePatternResponse> create(@Valid @RequestBody CreateUsagePatternRequest request) {
        UsagePattern created = usagePatternService.createPattern(
                request.userId(),
                request.type(),
                request.title(),
                request.desc(),
                request.location(),
                request.impact(),
                request.tag(),
                request.time()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Obtener patrón por ID", description = "Recupera los detalles de un patrón por su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patrón encontrado", content = @Content(schema = @Schema(implementation = UsagePatternResponse.class))),
            @ApiResponse(responseCode = "404", description = "Patrón no encontrado")
    })
    @GetMapping("/{patternId}")
    public ResponseEntity<UsagePatternResponse> getById(@PathVariable Integer patternId) {
        return usagePatternService.getById(patternId)
                .map(pattern -> ResponseEntity.ok(toResponse(pattern)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar patrones por usuario", description = "Retorna todos los patrones analíticos identificados para un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colección de patrones obtenida")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UsagePatternResponse>> getByUserId(@PathVariable Integer userId) {
        List<UsagePatternResponse> data = usagePatternService.getByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Listar todos los patrones de uso", description = "Retorna el catálogo global de patrones detectados en la plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista completa de patrones")
    })
    @GetMapping
    public ResponseEntity<List<UsagePatternResponse>> getAll() {
        List<UsagePatternResponse> data = usagePatternService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Actualizar estado de atención del patrón", description = "Cambia el estado de un patrón a RESOLVED, DISMISSED, o INVESTIGATING.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado del patrón actualizado", content = @Content(schema = @Schema(implementation = UsagePatternResponse.class))),
            @ApiResponse(responseCode = "404", description = "Patrón no encontrado")
    })
    @PutMapping("/{patternId}/status")
    public ResponseEntity<UsagePatternResponse> updateStatus(
            @PathVariable Integer patternId,
            @Valid @RequestBody UpdateUsagePatternStatusRequest request
    ) {
        UsagePattern updated = usagePatternService.updateStatus(patternId, request.status());
        return ResponseEntity.ok(toResponse(updated));
    }

    @Operation(summary = "Eliminar patrón de uso", description = "Remueve un patrón del catálogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Patrón eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Patrón no encontrado")
    })
    @DeleteMapping("/{patternId}")
    public ResponseEntity<Void> delete(@PathVariable Integer patternId) {
        usagePatternService.deleteById(patternId);
        return ResponseEntity.noContent().build();
    }

    private UsagePatternResponse toResponse(UsagePattern pattern) {
        return new UsagePatternResponse(
                pattern.patternId(),
                pattern.userId(),
                pattern.type(),
                pattern.title(),
                pattern.desc(),
                pattern.location(),
                pattern.impact(),
                pattern.tag(),
                pattern.time()
        );
    }
}
