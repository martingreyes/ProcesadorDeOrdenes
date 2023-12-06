package ar.edu.um.programacion2.martin.repository;

import ar.edu.um.programacion2.martin.domain.Orden;
import ar.edu.um.programacion2.martin.service.dto.OrdenDTO;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orden entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    // @Query(value = "SELECT * FROM Orden WHERE modo = :modo AND analisis IS NULL", nativeQuery = true)
    // List<Orden> findOrdenesNullByModo(@Param("modo") String modo);

    List<Orden> findByModoAndAnalisisIsNull(String modo);

    @Query(
        "SELECT o FROM Orden o " +
        "WHERE (:procesamiento IS NULL OR o.procesamiento = :procesamiento) " +
        "AND (:clienteId IS NULL OR o.cliente = :clienteId) " +
        "AND (:accionId IS NULL OR o.accionId = :accionId) " +
        "AND (:fechaInicio IS NULL OR o.fechaOperacion >= :fechaInicio) " +
        "AND (:fechaFin IS NULL OR o.fechaOperacion <= :fechaFin)"
    )
    List<Orden> findOrdenesByFilters(
        @Param("procesamiento") Boolean procesamiento,
        @Param("clienteId") Long clienteId,
        @Param("accionId") Long accionId,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    List<Orden> findByProcesamientoAndClienteAndAccionIdAndFechaOperacionBetween(
        Boolean procesamiento,
        Long cliente,
        Long accionId,
        Instant fechaInicio,
        Instant fechaFin
    );

    List<Orden> findByProcesamientoAndFechaOperacionBetween(Boolean procesamiento, Instant fechaInicio, Instant fechaFin);

    List<Orden> findByProcesamientoAndFechaOperacionAfter(Boolean procesamiento, Instant fechaInicio);

    List<Orden> findByProcesamientoAndFechaOperacionBefore(Boolean procesamiento, Instant fechaFin);

    List<Orden> findByProcesamientoAndClienteAndAccionId(Boolean procesamiento, Long cliente, Long accionId);
}
