package com.pedidos360.ms_productos.event;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrdenCreadaEvent {

    private Long ordenId;
    private String usuarioId;
    private String email;
    private BigDecimal total;
    private String fechaCreacion;
    private List<OrdenItemEvent> items = new ArrayList<>();

    public OrdenCreadaEvent() {
    }

    public Long getOrdenId() { return ordenId; }
    public void setOrdenId(Long ordenId) { this.ordenId = ordenId; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<OrdenItemEvent> getItems() { return items; }
    public void setItems(List<OrdenItemEvent> items) { this.items = items; }
}