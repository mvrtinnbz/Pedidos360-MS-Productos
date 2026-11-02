package com.pedidos360.ms_productos.controller;

import com.pedidos360.ms_productos.entity.Producto;
import com.pedidos360.ms_productos.kafka.TiendaEvento;
import com.pedidos360.ms_productos.kafka.TiendaEventoPublisher;
import com.pedidos360.ms_productos.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;
    private final TiendaEventoPublisher eventoPublisher;

    public ProductoController(ProductoService service, TiendaEventoPublisher eventoPublisher) {
        this.service = service;
        this.eventoPublisher = eventoPublisher;
    }

    @GetMapping
    public List<Producto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        Producto producto = service.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(producto);
    }

    // Registra que alguien vio el producto -> evento PRODUCTO_VISTO en Kafka.
    // Publico: con token se registra el usuario; sin token queda como "anonimo".
    @PostMapping("/{id}/vista")
    public ResponseEntity<Void> registrarVista(@PathVariable Long id,
                                               @AuthenticationPrincipal Jwt jwt) {
        Producto producto = service.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        eventoPublisher.publicar(TiendaEvento.productoVisto(producto, usuarioDesde(jwt)));

        return ResponseEntity.accepted().build();
    }

    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        Producto nuevo = service.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(
            @PathVariable Long id,
            @RequestBody Producto detalles) {

        Producto producto = service.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        producto.setNombre(detalles.getNombre());
        producto.setDescripcion(detalles.getDescripcion());
        producto.setPrecio(detalles.getPrecio());
        producto.setStock(detalles.getStock());
        producto.setCategoria(detalles.getCategoria());

        return ResponseEntity.ok(service.save(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Producto producto = service.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    // Mismo usuarioId que usan los otros microservicios (UPN / correo del token)
    private static String usuarioDesde(Jwt jwt) {
        if (jwt == null) {
            return "anonimo";
        }
        for (String claim : new String[]{"preferred_username", "upn", "unique_name", "email"}) {
            String valor = jwt.getClaimAsString(claim);
            if (valor != null && !valor.isBlank()) {
                return valor;
            }
        }
        return jwt.getSubject();
    }
}