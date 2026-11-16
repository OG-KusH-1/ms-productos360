package cl.duoc.pedidos360.productos.streaming.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO tipado para eventos de telemetría de catálogo/productos hacia Kafka.
 * Tópico: pedidos360.analytics.metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsMetricEventDTO {

    private String metricId;
    private String sourceService;
    private String category;
    private Long entityId;
    private String entityName;
    private BigDecimal amount;
    private Integer quantityDelta;
    private String details;
    private Instant timestamp;
}
