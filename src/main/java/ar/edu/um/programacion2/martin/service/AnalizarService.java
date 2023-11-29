package ar.edu.um.programacion2.martin.service;

import ar.edu.um.programacion2.martin.domain.Orden;
import ar.edu.um.programacion2.martin.service.dto.OrdenDTO;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnalizarService {

    private final Logger log = LoggerFactory.getLogger(AnalizarService.class);

    @Value("${miapp.url-clientes}")
    public String urlClientes;

    @Value("${miapp.url-acciones}")
    public String urlAcciones;

    @Value("${miapp.url-cantidad}")
    public String urlCantidad;

    @Value("${miapp.url-ultimovalor}")
    private String urlUltimoValor;

    @Value("${miapp.token}")
    public String token;

    @Autowired
    OrdenService ordenService;

    @Autowired
    CatedraService catedraService;

    @Autowired
    ColaAhoraService colaAhoraService;

    @Autowired
    ColaPrincipioDiaService colaPrincipioDiaService;

    @Autowired
    ColaFinDiaService colaFinDiaService;

    public void agregarOrdenesACola(String modo) {
        if (modo.equals("PRINCIPIODIA")) {
            List<Orden> ordenes = ordenService.findOrdenesNullByModo(modo);
            for (Orden orden : ordenes) {
                colaPrincipioDiaService.agregarOrden(orden);
                log.info(colaPrincipioDiaService.tamanoCola() + " ordenes en la cola PrincipioDia");
            }
        } else if (modo.equals("FINDIA")) {
            List<Orden> ordenes = ordenService.findOrdenesNullByModo(modo);
            for (Orden orden : ordenes) {
                colaFinDiaService.agregarOrden(orden);
            }
            log.info(colaFinDiaService.tamanoCola() + " ordenes en la cola FinDia");
        } else {
            List<Orden> ordenes = ordenService.findOrdenesNullByModo(modo);
            for (Orden orden : ordenes) {
                colaAhoraService.agregarOrden(orden);
            }
            log.info(colaAhoraService.tamanoCola() + " ordenes en la cola Ahora");
        }
    }

    public boolean analizarHorario(Orden orden) {
        boolean valido = true;

        Instant instant = orden.getFechaOperacion();

        ZonedDateTime fecha = instant.atZone(ZoneId.of("UTC"));

        LocalTime horaLocal = fecha.toLocalTime();

        LocalTime horaInicio = LocalTime.of(9, 0);
        LocalTime horaFin = LocalTime.of(18, 0);

        if (horaLocal.isAfter(horaInicio) && horaLocal.isBefore(horaFin)) {
            valido = true;
        } else {
            orden.setDescripcion(orden.getDescripcion() + "Una orden instantánea no puede ejecutarse fuera del horario de transacciones. ");
            valido = false;
        }
        return valido;
    }

    public boolean analizarCliente(Orden orden) {
        boolean valido = false;
        JsonNode clientesNode = catedraService.getClientes();
        if (clientesNode != null && clientesNode.isArray()) {
            for (JsonNode cliente : clientesNode) {
                long clienteId = cliente.get("id").asLong();
                if (clienteId == orden.getCliente()) {
                    valido = true;
                    break;
                }
            }
            if (!valido) {
                orden.setDescripcion(orden.getDescripcion() + "El cliente no existe. ");
            }
        }
        return valido;
    }

    public boolean analizarAccion(Orden orden) {
        boolean valido = false;
        JsonNode accionesNode = catedraService.getAcciones();
        if (accionesNode != null && accionesNode.isArray()) {
            for (JsonNode accion : accionesNode) {
                long accionId = accion.get("id").asLong();
                if (accionId == orden.getAccionId()) {
                    valido = true;
                    break;
                }
            }
            if (!valido) {
                orden.setDescripcion(orden.getDescripcion() + "La accion no existe. ");
            }
        }
        return valido;
    }

    public boolean analizarCantidad(Orden orden) {
        Integer cantidadActual = null;
        boolean valido = false;
        JsonNode cantidadActualNode = catedraService.getCantidad(orden);
        if (cantidadActualNode != null) {
            cantidadActual = cantidadActualNode.asInt();
            if (cantidadActual >= orden.getCantidad()) {
                valido = true;
            }
        }
        if (!valido) {
            orden.setDescripcion(orden.getDescripcion() + "La cantidad actual no es suficiente. ");
        }
        return valido;
    }

    public void cambiarPrecio(Orden orden) {
        JsonNode ultimoValorNode = catedraService.getPrecio(orden);
        if (ultimoValorNode != null) {
            Double valor = ultimoValorNode.get("valor").asDouble();
            orden.setPrecio(valor);
        }
    }

    public Map<String, List<Orden>> analizarOrdenes(String modo) {
        log.info("Analizando ordenes con modo " + modo + " ...");
        Map<String, List<Orden>> mapaListas = new HashMap<>();

        List<Orden> aceptadas = new ArrayList<>();

        List<Orden> rechazadas = new ArrayList<>();

        agregarOrdenesACola(modo);

        if (modo.equals("PRINCIPIODIA")) {
            List<Orden> ordenes = colaPrincipioDiaService.obtenerElementosDeCola();
            for (Orden orden : ordenes) {
                cambiarPrecio(orden);
                boolean condicion2 = analizarCliente(orden);
                boolean condicion3 = analizarAccion(orden);
                boolean condicion4 = true;
                if (orden.getOperacion().equals("VENTA")) {
                    condicion4 = analizarCantidad(orden);
                }
                if (condicion2 && condicion3 && condicion4) {
                    orden.setAnalisis(true);
                    orden.setDescripcion("Analsis Valido.");
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                    aceptadas.add(orden);
                } else {
                    orden.setAnalisis(false);
                    orden.setProcesamiento(false);
                    orden.setDescripcion("Analsis Invalido. " + orden.getDescripcion());
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                    rechazadas.add(orden);
                }
            }
        } else if (modo.equals("FINDIA")) {
            List<Orden> ordenes = colaFinDiaService.obtenerElementosDeCola();
            for (Orden orden : ordenes) {
                cambiarPrecio(orden);
                boolean condicion2 = analizarCliente(orden);
                boolean condicion3 = analizarAccion(orden);
                boolean condicion4 = true;
                if (orden.getOperacion().equals("VENTA")) {
                    condicion4 = analizarCantidad(orden);
                }
                if (condicion2 && condicion3 && condicion4) {
                    orden.setAnalisis(true);
                    orden.setDescripcion("Analsis Valido.");
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                    aceptadas.add(orden);
                } else {
                    orden.setAnalisis(false);
                    orden.setProcesamiento(false);
                    orden.setDescripcion("Analsis Invalido. " + orden.getDescripcion());
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                    rechazadas.add(orden);
                }
            }
        } else {
            List<Orden> ordenes = colaAhoraService.obtenerElementosDeCola();
            for (Orden orden : ordenes) {
                boolean condicion1 = analizarHorario(orden);
                boolean condicion2 = analizarCliente(orden);
                boolean condicion3 = analizarAccion(orden);
                boolean condicion4 = true;
                if (orden.getOperacion().equals("VENTA")) {
                    condicion4 = analizarCantidad(orden);
                }

                if (condicion1 && condicion2 && condicion3 && condicion4) {
                    orden.setAnalisis(true);
                    orden.setDescripcion("Analsis Valido.");
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                    aceptadas.add(orden);
                } else {
                    orden.setAnalisis(false);
                    orden.setProcesamiento(false);
                    orden.setDescripcion("Analsis Invalido. " + orden.getDescripcion());
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                    rechazadas.add(orden);
                }
            }
        }

        mapaListas.put("Ordenes Aceptadas", aceptadas);
        mapaListas.put("Ordenes Rechazadas", rechazadas);

        log.info(aceptadas.size() + " ordenes con modo " + modo + " aceptadas");
        log.info(rechazadas.size() + " ordenes con modo " + modo + " rechazadas");

        return mapaListas;
    }
}
