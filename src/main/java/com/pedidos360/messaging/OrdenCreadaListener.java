package com.pedidos360.ms_productos.messaging;

import com.pedidos360.ms_productos.config.RabbitMQConfig;
import com.pedidos360.ms_productos.event.OrdenCreadaEvent;
import com.pedidos360.ms_productos.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrdenCreadaListener {

    private static final Logger log = LoggerFactory.getLogger(OrdenCreadaListener.class);

    private final ProductoService service;

    public OrdenCreadaListener(ProductoService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PRODUCTOS_STOCK)
    public void recibirOrdenCreada(OrdenCreadaEvent evento) {

        log.info("Evento orden.creada recibido desde RabbitMQ -> orden {} ({} items)",
                evento.getOrdenId(), evento.getItems().size());

        try {
            service.descontarStock(evento);
        } catch (Exception e) {
            // Se captura para que RabbitMQ no reintente el mismo mensaje en bucle.
            log.error("No se pudo descontar el stock de la orden {}: {}",
                    evento.getOrdenId(), e.getMessage());
        }
    }
}