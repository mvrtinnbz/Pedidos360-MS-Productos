package com.pedidos360.ms_productos.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

// Publica los eventos de interaccion en Kafka.
// Si Kafka falla, solo se registra una advertencia: el catalogo sigue funcionando.
@Component
public class TiendaEventoPublisher {

    private static final Logger log = LoggerFactory.getLogger(TiendaEventoPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonMapper jsonMapper;

    public TiendaEventoPublisher(KafkaTemplate<String, String> kafkaTemplate, JsonMapper jsonMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.jsonMapper = jsonMapper;
    }

    public void publicar(TiendaEvento evento) {
        try {
            String json = jsonMapper.writeValueAsString(evento);

            kafkaTemplate.send(KafkaConfig.TOPICO_TIENDA_EVENTOS, String.valueOf(evento.getProductoId()), json)
                    .whenComplete((resultado, error) -> {
                        if (error != null) {
                            log.warn("No se pudo publicar {} en Kafka: {}", evento.getTipo(), error.getMessage());
                        } else {
                            log.info("Evento Kafka [{}] -> producto {} (usuario {})",
                                    evento.getTipo(), evento.getProductoId(), evento.getUsuarioId());
                        }
                    });

        } catch (Exception e) {
            log.warn("No se pudo publicar {} en Kafka: {}", evento.getTipo(), e.getMessage());
        }
    }
}