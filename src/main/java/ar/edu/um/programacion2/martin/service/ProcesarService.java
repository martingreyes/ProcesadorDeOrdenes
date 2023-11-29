package ar.edu.um.programacion2.martin.service;

import ar.edu.um.programacion2.martin.domain.Orden;
import ar.edu.um.programacion2.martin.service.dto.OrdenDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProcesarService {

    private final Logger log = LoggerFactory.getLogger(ProcesarService.class);

    @Autowired
    OrdenService ordenService;

    @Autowired
    ColaAhoraService colaAhoraService;

    @Autowired
    ColaPrincipioDiaService colaPrincipioDiaService;

    @Autowired
    ColaFinDiaService colaFinDiaService;

    public boolean comprar(Orden orden) {
        orden.setProcesamiento(true);
        orden.setDescripcion(orden.getDescripcion() + " Compra Exitosa.");
        log.info("Procesando compra");
        return true;
    }

    public boolean vender(Orden orden) {
        //TODO
        orden.setProcesamiento(true);
        orden.setDescripcion(orden.getDescripcion() + " Venta Exitosa.");
        log.info("Procesando venta");
        return true;
    }

    public Map<String, List<Orden>> procesarOrdenes(String modo) {
        log.info("Procesando ordenes con modo " + modo + " ...");
        Map<String, List<Orden>> mapaListas = new HashMap<>();

        List<Orden> procesadas = new ArrayList<>();

        List<Orden> no_procesadas = new ArrayList<>();

        if (modo.equals("PRINCIPIODIA")) {
            List<Orden> ordenes = colaPrincipioDiaService.obtenerElementosDeCola();
            for (Orden orden : ordenes) {
                if (orden.getAnalisis()) {
                    if (orden.getOperacion().equals("COMPRA")) {
                        if (comprar(orden)) {
                            procesadas.add(orden);
                        } else {
                            no_procesadas.add(orden);
                        }
                    } else {
                        if (vender(orden)) {
                            procesadas.add(orden);
                        } else {
                            no_procesadas.add(orden);
                        }
                    }
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                }
            }
        } else if (modo.equals("FINDIA")) {
            List<Orden> ordenes = colaFinDiaService.obtenerElementosDeCola();
            for (Orden orden : ordenes) {
                if (orden.getAnalisis()) {
                    if (orden.getOperacion().equals("COMPRA")) {
                        if (comprar(orden)) {
                            procesadas.add(orden);
                        } else {
                            no_procesadas.add(orden);
                        }
                    } else {
                        if (vender(orden)) {
                            procesadas.add(orden);
                        } else {
                            no_procesadas.add(orden);
                        }
                    }
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                }
            }
        } else {
            List<Orden> ordenes = colaAhoraService.obtenerElementosDeCola();
            for (Orden orden : ordenes) {
                if (orden.getAnalisis()) {
                    if (orden.getOperacion().equals("COMPRA")) {
                        if (comprar(orden)) {
                            procesadas.add(orden);
                        } else {
                            no_procesadas.add(orden);
                        }
                    } else {
                        if (vender(orden)) {
                            procesadas.add(orden);
                        } else {
                            no_procesadas.add(orden);
                        }
                    }
                    OrdenDTO ordenDTO = ordenService.toDTO(orden);
                    ordenService.update(ordenDTO);
                }
            }
        }

        mapaListas.put("Ordenes Procesadas", procesadas);
        mapaListas.put("Ordenes No Procesadas", no_procesadas);

        log.info(procesadas.size() + " ordenes con modo " + modo + " procesadas");
        log.info(no_procesadas.size() + " ordenes con modo " + modo + " no procesadas");

        return mapaListas;
    }
}
