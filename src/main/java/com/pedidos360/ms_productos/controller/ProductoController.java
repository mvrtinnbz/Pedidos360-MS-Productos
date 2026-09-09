package com.pedidos360.ms_productos.controller;

import com.pedidos360.ms_productos.entity.Producto;
import com.pedidos360.ms_productos.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // USER y ADMIN pueden consultar productos
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<Producto> getAll() {
        return service.findAll();
    }

    // USER y ADMIN pueden consultar un producto
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        Producto producto = service.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(producto);
    }

    // Solo ADMIN puede crear
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        Producto nuevo = service.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // Solo ADMIN puede modificar
    @PreAuthorize("hasRole('ADMIN')")
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

        return ResponseEntity.ok(service.save(producto));
    }

    // Solo ADMIN puede eliminar
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Producto producto = service.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}