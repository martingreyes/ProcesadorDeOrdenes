package ar.edu.um.programacion2.martin.service;

import ar.edu.um.programacion2.martin.domain.Orden;
import ar.edu.um.programacion2.martin.repository.OrdenRepository;
import ar.edu.um.programacion2.martin.service.dto.OrdenDTO;
import ar.edu.um.programacion2.martin.service.mapper.OrdenMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Orden}.
 */
@Service
@Transactional
public class OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenService.class);

    private final OrdenRepository ordenRepository;

    private final OrdenMapper ordenMapper;

    public OrdenService(OrdenRepository ordenRepository, OrdenMapper ordenMapper) {
        this.ordenRepository = ordenRepository;
        this.ordenMapper = ordenMapper;
    }

    /**
     * Save a orden.
     *
     * @param ordenDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdenDTO save(OrdenDTO ordenDTO) {
        log.debug("Request to save Orden : {}", ordenDTO);
        Orden orden = ordenMapper.toEntity(ordenDTO);
        orden = ordenRepository.save(orden);
        return ordenMapper.toDto(orden);
    }

    public void deleteAll() {
        log.debug("Request para borrar todas las Ordenes");
        ordenRepository.deleteAll();
    }

    public List<Orden> findOrdenesNullByModo(String modo) {
        log.debug("Request to get all Ordenes with estado Pendiente");
        List<Orden> ordenes = ordenRepository.findByModoAndAnalisisIsNull(modo);
        return ordenes;
    }

    public List<Orden> findOrdenesByFilters(Boolean procesamiento, Long cliente, Long accionId, Instant fechaInicio, Instant fechaFin) {
        if (cliente == null && accionId == null && fechaFin == null && fechaInicio != null) {
            return ordenRepository.findByProcesamientoAndFechaOperacionAfter(procesamiento, fechaInicio);
        } else if (cliente == null && accionId == null && fechaInicio == null && fechaFin != null) {
            return ordenRepository.findByProcesamientoAndFechaOperacionBefore(procesamiento, fechaFin);
        } else if (cliente == null && accionId == null && fechaInicio != null && fechaFin != null) {
            return ordenRepository.findByProcesamientoAndFechaOperacionBetween(procesamiento, fechaInicio, fechaFin);
        } else if (cliente != null && accionId != null && fechaInicio == null && fechaFin == null) {
            return ordenRepository.findByProcesamientoAndClienteAndAccionId(procesamiento, cliente, accionId);
        } else {
            return ordenRepository.findByProcesamientoAndClienteAndAccionIdAndFechaOperacionBetween(
                procesamiento,
                cliente,
                accionId,
                fechaInicio,
                fechaFin
            );
        }
    }

    /**
     * Update a orden.
     *
     * @param ordenDTO the entity to save.
     * @return the persisted entity.
     */
    public OrdenDTO update(OrdenDTO ordenDTO) {
        log.debug("Request to update Orden : {}", ordenDTO);
        Orden orden = ordenMapper.toEntity(ordenDTO);
        orden = ordenRepository.save(orden);
        return ordenMapper.toDto(orden);
    }

    /**
     * Partially update a orden.
     *
     * @param ordenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrdenDTO> partialUpdate(OrdenDTO ordenDTO) {
        log.debug("Request to partially update Orden : {}", ordenDTO);

        return ordenRepository
            .findById(ordenDTO.getId())
            .map(existingOrden -> {
                ordenMapper.partialUpdate(existingOrden, ordenDTO);

                return existingOrden;
            })
            .map(ordenRepository::save)
            .map(ordenMapper::toDto);
    }

    /**
     * Get all the ordens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrdenDTO> findAll() {
        log.debug("Request to get all Ordens");
        return ordenRepository.findAll().stream().map(ordenMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one orden by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrdenDTO> findOne(Long id) {
        log.debug("Request to get Orden : {}", id);
        return ordenRepository.findById(id).map(ordenMapper::toDto);
    }

    /**
     * Delete the orden by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Orden : {}", id);
        ordenRepository.deleteById(id);
    }

    public OrdenDTO toDTO(Orden orden) {
        OrdenDTO ordenDTO = new OrdenDTO();
        ordenDTO.setId(orden.getId());
        ordenDTO.setCliente(orden.getCliente());
        ordenDTO.setAccionId(orden.getAccionId());
        ordenDTO.setAccion(orden.getAccion());
        ordenDTO.setOperacion(orden.getOperacion());
        ordenDTO.setPrecio(orden.getPrecio());
        ordenDTO.setCantidad(orden.getCantidad());
        ordenDTO.setFechaOperacion(orden.getFechaOperacion());
        ordenDTO.setModo(orden.getModo());
        ordenDTO.setAnalisis(orden.getAnalisis());
        ordenDTO.setProcesamiento(orden.getProcesamiento());
        ordenDTO.setDescripcion(orden.getDescripcion());

        return ordenDTO;
    }

    public JsonNode toJson(Orden orden) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("cliente", orden.getCliente());
        jsonNode.put("accionId", orden.getAccionId());
        jsonNode.put("accion", orden.getAccion());
        jsonNode.put("operacion", orden.getOperacion());
        jsonNode.put("cantidad", orden.getCantidad());
        jsonNode.put("precio", orden.getPrecio());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneId.of("UTC"));
        String formattedString = formatter.format(orden.getFechaOperacion());
        jsonNode.put("fechaOperacion", formattedString);
        jsonNode.put("modo", orden.getModo());
        if (orden.getProcesamiento() != null && orden.getProcesamiento()) {
            jsonNode.put("operacionExitosa", true);
        } else {
            jsonNode.put("operacionExitosa", false);
        }
        jsonNode.put("operacionObservaciones", orden.getDescripcion());

        return jsonNode;
    }
}
