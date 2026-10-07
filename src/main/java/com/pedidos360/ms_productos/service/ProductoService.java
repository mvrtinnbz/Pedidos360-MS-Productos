package com.pedidos360.ms_productos.service;

import com.pedidos360.ms_productos.entity.Producto;
import com.pedidos360.ms_productos.event.OrdenCreadaEvent;
import com.pedidos360.ms_productos.event.OrdenItemEvent;
import com.pedidos360.ms_productos.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> findAll() {
        return repository.findAll();
    }

    public Producto findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Producto save(Producto producto) {
        return repository.save(producto);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Descuenta el stock de cada producto de la orden recibida por RabbitMQ.
    @Transactional
    public void descontarStock(OrdenCreadaEvent evento) {

        for (OrdenItemEvent item : evento.getItems()) {

            Producto producto = repository.findById(item.getProductoId()).orElse(null);

            if (producto == null) {
                log.warn("Orden {}: el producto {} no existe, se omite",
                        evento.getOrdenId(), item.getProductoId());
                continue;
            }

            int stockAnterior = producto.getStock() != null ? producto.getStock() : 0;
            int nuevoStock = Math.max(0, stockAnterior - item.getCantidad());

            producto.setStock(nuevoStock);
            repository.save(producto);

            log.info("Orden {}: stock de '{}' (id {}) actualizado {} -> {}",
                    evento.getOrdenId(), producto.getNombre(), producto.getId(),
                    stockAnterior, nuevoStock);
        }
    }
}