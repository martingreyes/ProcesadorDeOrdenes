package ar.edu.um.programacion2.martin.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link ar.edu.um.programacion2.martin.domain.Orden} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdenDTO implements Serializable {

    private Long id;

    private Long cliente;

    private Long accionId;

    private String accion;

    private String operacion;

    private Double precio;

    private Integer cantidad;

    private Instant fechaOperacion;

    private String modo;

    private Boolean analisis;

    private Boolean procesamiento;

    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }

    public Long getAccionId() {
        return accionId;
    }

    public void setAccionId(Long accionId) {
        this.accionId = accionId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Instant getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(Instant fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public Boolean getAnalisis() {
        return analisis;
    }

    public void setAnalisis(Boolean analisis) {
        this.analisis = analisis;
    }

    public Boolean getProcesamiento() {
        return procesamiento;
    }

    public void setProcesamiento(Boolean procesamiento) {
        this.procesamiento = procesamiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdenDTO)) {
            return false;
        }

        OrdenDTO ordenDTO = (OrdenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdenDTO{" +
            "id=" + getId() +
            ", cliente=" + getCliente() +
            ", accionId=" + getAccionId() +
            ", accion='" + getAccion() + "'" +
            ", operacion='" + getOperacion() + "'" +
            ", precio=" + getPrecio() +
            ", cantidad=" + getCantidad() +
            ", fechaOperacion='" + getFechaOperacion() + "'" +
            ", modo='" + getModo() + "'" +
            ", analisis='" + getAnalisis() + "'" +
            ", procesamiento='" + getProcesamiento() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
