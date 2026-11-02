package com.pedidos360.ms_productos.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    // Topico compartido: ms-productos y ms-carrito publican aqui
    public static final String TOPICO_TIENDA_EVENTOS = "tienda.eventos";

    // Crea el topico si no existe (util en AWS)
    @Bean
    public NewTopic topicoTiendaEventos() {
        return TopicBuilder.name(TOPICO_TIENDA_EVENTOS)
                .partitions(1)
                .replicas(1)
                .build();
    }
}