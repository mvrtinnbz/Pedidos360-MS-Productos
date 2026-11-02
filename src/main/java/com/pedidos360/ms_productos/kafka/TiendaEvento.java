package com.pedidos360.ms_productos.kafka;

import com.pedidos360.ms_productos.entity.Producto;

import java.time.Instant;

// Evento de interaccion que viaja por Kafka (topico tienda.eventos).
// Mismo formato en ms-productos y ms-carrito.
public class TiendaEvento {

    public static final String PRODUCTO_VISTO = "PRODUCTO_VISTO";

    private String tipo;
    private String usuarioId;
    private Long productoId;
    private String nombreProducto;
    private String categoria;
    private String origen;
    private String fecha;

    public TiendaEvento() {
    }

    public static TiendaEvento productoVisto(Producto producto, String usuarioId) {
        TiendaEvento evento = new TiendaEvento();
        evento.tipo = PRODUCTO_VISTO;
        evento.usuarioId = usuarioId;
        evento.productoId = producto.getId();
        evento.nombreProducto = producto.getNombre();
        evento.categoria = producto.getCategoria();
        evento.origen = "ms-productos";
        evento.fecha = Instant.now().toString();
        return evento;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}