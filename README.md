# smartdrop-analytics-service

> **SmartDrop - IoT Liquid Monitoring & Quality Management**  
> *UPC - Fundamentos de Arquitectura de Software (2026-20)*  
> *Autor Responsable:* **Andy Saul Pillaca Gonzales**

---

## Descripcion General

Microservicio analitico orientado al procesamiento de telemetria en tiempo real, aplicando el patron de diseno Strategy para la deteccion temprana de fugas nocturnas en agua potable y fluctuaciones termicas en fermentacion cervecera.

---

## Ejecucion en Entorno Local

Para compilar y ejecutar el proyecto localmente sin preconfiguraciones externas:

```powershell
# Compilacion y arranque con Maven Wrapper
./mvnw spring-boot:run
```

## Configuracion de Puertos y Endpoints

* **Puerto Local:** 8083
* **Swagger UI:** [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)
* **OpenAPI Especificacion JSON:** [http://localhost:8083/v3/api-docs](http://localhost:8083/v3/api-docs)
* **Health Check Liveness Probe:** [http://localhost:8083/api/v1/health](http://localhost:8083/api/v1/health)

---

## Pruebas Automatizadas

Para validar la suite de pruebas unitarias y de integracion:

```powershell
./mvnw test
```