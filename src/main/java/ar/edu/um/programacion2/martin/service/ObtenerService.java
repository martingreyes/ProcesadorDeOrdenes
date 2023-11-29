package ar.edu.um.programacion2.martin.service;

import ar.edu.um.programacion2.martin.domain.Orden;
import ar.edu.um.programacion2.martin.service.dto.OrdenDTO;
import ar.edu.um.programacion2.martin.service.dto.OrdenesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Collections;
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
public class ObtenerService {

    private final Logger log = LoggerFactory.getLogger(ObtenerService.class);

    @Value("${miapp.url-ordenes-local}")
    private String urlOrdenesLocal;

    @Value("${miapp.url-ordenes}")
    private String urlOrdenes;

    @Value("${miapp.token}")
    private String token;

    @Autowired
    OrdenService ordenService;

    @Autowired
    CatedraService catedraService;

    public List<Orden> mapearRespuestaAOrdenes(HttpResponse<String> jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            OrdenesDTO response = objectMapper.readValue(jsonResponse.body(), OrdenesDTO.class);
            List<Orden> ordenes = response.getOrdenes();
            return ordenes;
        } catch (IOException e) {
            //e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void almacenarOrdenesEnBd(List<Orden> ordenes) {
        for (Orden orden : ordenes) {
            orden.setDescripcion("");
            OrdenDTO ordenDTO = ordenService.toDTO(orden);
            ordenService.save(ordenDTO);
        }
    }

    public Map<String, List<Orden>> obtenerOrdenes() {
        log.info("Obteniendo ordenes ...");
        HttpResponse<String> jsonResponse = catedraService.getOrdenes();
        List<Orden> ordenes = mapearRespuestaAOrdenes(jsonResponse);
        almacenarOrdenesEnBd(ordenes);
        log.info(ordenes.size() + " ordenes recuperadas");
        Map<String, List<Orden>> mapaLista = new HashMap<>();
        mapaLista.put("Ordenes Obtenidas", ordenes);
        return mapaLista;
    }
}
