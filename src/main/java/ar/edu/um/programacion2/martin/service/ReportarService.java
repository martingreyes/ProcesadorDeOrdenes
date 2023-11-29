package ar.edu.um.programacion2.martin.service;

import ar.edu.um.programacion2.martin.domain.Orden;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportarService {

    private final Logger log = LoggerFactory.getLogger(ReportarService.class);

    @Autowired
    ColaPrincipioDiaService colaPrincipioDiaService;

    @Autowired
    ColaFinDiaService colaFinDiaService;

    @Autowired
    ColaAhoraService colaAhoraService;

    @Autowired
    CatedraService catedraService;

    @Autowired
    OrdenService ordenService;

    @Value("${miapp.url-reportar}")
    private String urlReportar;

    @Value("${miapp.token}")
    private String token;

    public Map<String, Object> reportarOrdenes(String modo) {
        log.info("Reportando ordenes con modo " + modo + " ...");
        Map<String, ArrayNode> json = new HashMap<>();

        ArrayNode ordenes = JsonNodeFactory.instance.arrayNode();

        if (modo.equals("PRINCIPIODIA")) {
            while (!colaPrincipioDiaService.estaVacia()) {
                Orden orden = colaPrincipioDiaService.quitarOrden();
                JsonNode ordenJson = ordenService.toJson(orden);
                ordenes.add(ordenJson);
            }
        } else if (modo.equals("FINDIA")) {
            while (!colaFinDiaService.estaVacia()) {
                Orden orden = colaFinDiaService.quitarOrden();
                JsonNode ordenJson = ordenService.toJson(orden);
                ordenes.add(ordenJson);
            }
        } else {
            while (!colaAhoraService.estaVacia()) {
                Orden orden = colaAhoraService.quitarOrden();
                JsonNode ordenJson = ordenService.toJson(orden);
                ordenes.add(ordenJson);
            }
        }

        json.put("ordenes", ordenes);

        HttpResponse<String> response = catedraService.postOrdenes(json);

        log.info("POST " + urlReportar + " : " + response.body());

        log.info(ordenes.size() + " ordenes con modo " + modo + " reportadas");

        Map<String, Object> resultadoFinal = new HashMap<>();

        resultadoFinal.put("ordenes", ordenes);

        resultadoFinal.put("respuesta", response.body());

        return resultadoFinal;
    }
}
