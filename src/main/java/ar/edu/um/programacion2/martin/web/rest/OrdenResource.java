package ar.edu.um.programacion2.martin.web.rest;

import ar.edu.um.programacion2.martin.domain.Orden;
import ar.edu.um.programacion2.martin.repository.OrdenRepository;
import ar.edu.um.programacion2.martin.service.AnalizarService;
import ar.edu.um.programacion2.martin.service.ColaAhoraService;
import ar.edu.um.programacion2.martin.service.ColaFinDiaService;
import ar.edu.um.programacion2.martin.service.ColaPrincipioDiaService;
import ar.edu.um.programacion2.martin.service.ObtenerService;
import ar.edu.um.programacion2.martin.service.OrdenService;
import ar.edu.um.programacion2.martin.service.ProcesarService;
import ar.edu.um.programacion2.martin.service.ReportarService;
import ar.edu.um.programacion2.martin.service.dto.OrdenDTO;
import ar.edu.um.programacion2.martin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ar.edu.um.programacion2.martin.domain.Orden}.
 */
@RestController
@RequestMapping("/api")
public class OrdenResource {

    private final Logger log = LoggerFactory.getLogger(OrdenResource.class);

    private static final String ENTITY_NAME = "orden";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdenService ordenService;

    private final OrdenRepository ordenRepository;

    public OrdenResource(OrdenService ordenService, OrdenRepository ordenRepository) {
        this.ordenService = ordenService;
        this.ordenRepository = ordenRepository;
    }

    @Autowired
    ObtenerService obtenerService;

    @GetMapping("/ordenes/obtener")
    @Secured("ROLE_USER")
    public Map<String, List<Orden>> obtener() {
        log.info("REST para obtener las ordenes");
        Map<String, List<Orden>> obtenidas = obtenerService.obtenerOrdenes();
        return obtenidas;
    }

    @Autowired
    AnalizarService analizarService;

    @GetMapping("/ordenes/analizar/{modo}")
    @Secured("ROLE_USER")
    public Map<String, List<Orden>> analizar(@PathVariable(value = "modo", required = false) final Integer modo) {
        if (modo == 1) {
            log.info("REST para analizar las ordenes PRINCIPIODIA");
            Map<String, List<Orden>> analizadas = analizarService.analizarOrdenes("PRINCIPIODIA");
            return analizadas;
        } else if (modo == 2) {
            log.info("REST para analizar las ordenes FINDIA");
            Map<String, List<Orden>> analizadas = analizarService.analizarOrdenes("FINDIA");
            return analizadas;
        } else {
            log.info("REST para analizar las ordenes AHORA");
            Map<String, List<Orden>> analizadas = analizarService.analizarOrdenes("AHORA");
            return analizadas;
        }
    }

    @Autowired
    ProcesarService procesarService;

    @GetMapping("/ordenes/procesar/{modo}")
    @Secured("ROLE_USER")
    public Map<String, List<Orden>> procesar(@PathVariable(value = "modo", required = false) final Integer modo) {
        if (modo == 1) {
            log.info("REST para procesar las ordenes PRINCIPIODIA");
            Map<String, List<Orden>> procesadas = procesarService.procesarOrdenes("PRINCIPIODIA");
            return procesadas;
        } else if (modo == 2) {
            log.info("REST para procesar las ordenes FINDIA");
            Map<String, List<Orden>> procesadas = procesarService.procesarOrdenes("FINDIA");
            return procesadas;
        } else {
            log.info("REST para procesar las ordenes AHORA");
            Map<String, List<Orden>> procesadas = procesarService.procesarOrdenes("AHORA");
            return procesadas;
        }
    }

    @DeleteMapping("/ordenes/borrar")
    @Secured("ROLE_USER")
    public ResponseEntity<Void> borrarOrdenes() {
        log.info("REST para borrar todas las ordenes");
        ordenService.deleteAll();
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, "borrar")).build();
    }

    @Autowired
    ColaAhoraService colaAhoraService;

    @Autowired
    ColaPrincipioDiaService colaPrincipioDiaService;

    @Autowired
    ColaFinDiaService colaFinDiaService;

    @GetMapping("/ordenes/cola/{modo}")
    @Secured("ROLE_USER")
    public List<Orden> cola(@PathVariable(value = "modo", required = false) final Integer modo) {
        if (modo == 1) {
            log.info("REST para ver ordenes de la cola PRINCIPIODIA");
            List<Orden> cola = colaPrincipioDiaService.obtenerElementosDeCola();
            return cola;
        } else if (modo == 2) {
            log.info("REST para ver ordenes de la cola FINDIA");
            List<Orden> cola = colaFinDiaService.obtenerElementosDeCola();
            return cola;
        } else {
            log.info("REST para ver ordenes de la cola AHORA");
            List<Orden> cola = colaAhoraService.obtenerElementosDeCola();
            return cola;
        }
    }

    @Autowired
    ReportarService reportarService;

    @GetMapping("/ordenes/reportar/{modo}")
    @Secured("ROLE_USER")
    public Map<String, Object> reportar(@PathVariable(value = "modo", required = false) final Integer modo) {
        if (modo == 1) {
            log.info("REST para reportar las ordenes PRINCIPIODIA");
            Map<String, Object> reportadas = reportarService.reportarOrdenes("PRINCIPIODIA");
            return reportadas;
        } else if (modo == 2) {
            log.info("REST para reportar las ordenes FINDIA");
            Map<String, Object> reportadas = reportarService.reportarOrdenes("FINDIA");
            return reportadas;
        } else {
            log.info("REST para reportar las ordenes AHORA");
            Map<String, Object> reportadas = reportarService.reportarOrdenes("AHORA");
            return reportadas;
        }
    }

    @GetMapping("/ordenes/{procesamiento}/buscar")
    @Secured("ROLE_USER")
    public ResponseEntity<List<Orden>> findOrdenesByFilters(
        @PathVariable("procesamiento") Integer procesamientoInt,
        @RequestParam(required = false) Long cliente,
        @RequestParam(required = false) Long accionId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fechaFin
    ) {
        Boolean procesamiento = true;

        if (procesamientoInt == 1) {
            procesamiento = false;
            log.info("REST para ver las ordenes No procesadas");
        } else {
            log.info("REST para ver las ordenes procesadas");
        }

        List<Orden> ordenes = ordenService.findOrdenesByFilters(procesamiento, cliente, accionId, fechaInicio, fechaFin);
        return ResponseEntity.ok(ordenes);
    }

    /**
     * {@code POST  /ordens} : Create a new orden.
     *
     * @param ordenDTO the ordenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordenDTO, or with status {@code 400 (Bad Request)} if the orden has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordens")
    public ResponseEntity<OrdenDTO> createOrden(@RequestBody OrdenDTO ordenDTO) throws URISyntaxException {
        log.debug("REST request to save Orden : {}", ordenDTO);
        if (ordenDTO.getId() != null) {
            throw new BadRequestAlertException("A new orden cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdenDTO result = ordenService.save(ordenDTO);
        return ResponseEntity
            .created(new URI("/api/ordens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordens/:id} : Updates an existing orden.
     *
     * @param id the id of the ordenDTO to save.
     * @param ordenDTO the ordenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenDTO,
     * or with status {@code 400 (Bad Request)} if the ordenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordens/{id}")
    public ResponseEntity<OrdenDTO> updateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdenDTO ordenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Orden : {}, {}", id, ordenDTO);
        if (ordenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdenDTO result = ordenService.update(ordenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordens/:id} : Partial updates given fields of an existing orden, field will ignore if it is null
     *
     * @param id the id of the ordenDTO to save.
     * @param ordenDTO the ordenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordenDTO,
     * or with status {@code 400 (Bad Request)} if the ordenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ordenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordens/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdenDTO> partialUpdateOrden(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdenDTO ordenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Orden partially : {}, {}", id, ordenDTO);
        if (ordenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdenDTO> result = ordenService.partialUpdate(ordenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ordens} : get all the ordens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordens in body.
     */
    @GetMapping("/ordens")
    public List<OrdenDTO> getAllOrdens() {
        log.debug("REST request to get all Ordens");
        return ordenService.findAll();
    }

    /**
     * {@code GET  /ordens/:id} : get the "id" orden.
     *
     * @param id the id of the ordenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordens/{id}")
    public ResponseEntity<OrdenDTO> getOrden(@PathVariable Long id) {
        log.debug("REST request to get Orden : {}", id);
        Optional<OrdenDTO> ordenDTO = ordenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordenDTO);
    }

    /**
     * {@code DELETE  /ordens/:id} : delete the "id" orden.
     *
     * @param id the id of the ordenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordens/{id}")
    public ResponseEntity<Void> deleteOrden(@PathVariable Long id) {
        log.debug("REST request to delete Orden : {}", id);
        ordenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
