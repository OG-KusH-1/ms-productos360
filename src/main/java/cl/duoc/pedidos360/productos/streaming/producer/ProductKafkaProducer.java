package cl.duoc.pedidos360.productos.streaming.producer;

import cl.duoc.pedidos360.productos.entity.Product;
import cl.duoc.pedidos360.productos.streaming.dto.AnalyticsMetricEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Productor Kafka de ms-productos360.
 * Publica eventos de cambios de catálogo y stock en pedidos360.analytics.metrics.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.analytics-metrics}")
    private String analyticsMetricsTopic;

    public void publishProductEvent(Product product, String category, String details) {
        String key = product.getId() != null ? "product-" + product.getId() : UUID.randomUUID().toString();
        AnalyticsMetricEventDTO event = AnalyticsMetricEventDTO.builder()
                .metricId(UUID.randomUUID().toString())
                .sourceService("ms-productos360")
                .category(category)
                .entityId(product.getId())
                .entityName(product.getNombre())
                .amount(product.getPrecio())
                .quantityDelta(product.getStock())
                .details(details)
                .timestamp(Instant.now())
                .build();

        kafkaTemplate.send(analyticsMetricsTopic, key, event).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[PRODUCT-KAFKA-PRODUCER] Error publicando evento {} para producto #{}: {}",
                        category, product.getId(), ex.getMessage());
            } else {
                log.info("[PRODUCT-KAFKA-PRODUCER] Evento '{}' publicado en topic='{}', partition={}, offset={}",
                        category,
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
