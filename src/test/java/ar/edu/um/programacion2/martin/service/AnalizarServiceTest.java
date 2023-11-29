package ar.edu.um.programacion2.martin.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ar.edu.um.programacion2.martin.domain.Orden;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
public class AnalizarServiceTest {

    @InjectMocks
    @Spy
    private AnalizarService analizarService;

    @Mock
    private CatedraService catedraService;

    private ObjectMapper mapper;

    @Value("${miapp.url-clientes}")
    private String urlClientes;

    @Value("${miapp.url-acciones}")
    private String urlAcciones;

    @Value("${miapp.url-cantidad}")
    private String urlCantidad;

    @Value("${miapp.token}")
    private String token;

    private JsonNode jsonClientes;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper();
        setJsonClientes();
        analizarService.token =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXJ0aW5yZXllcyIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE3Mjk2ODg3ODV9.6yeagJAm9m8cV_gjHu8e6UPlTkdS8iqkX4_GlvwKrqshcpIqs6URQFyBAYI2uEKgiW6Zvn5E5x6HEhPDkecQVg";
        analizarService.urlClientes = "http://192.168.194.254:8000/api/clientes/";
        analizarService.urlAcciones = "http://192.168.194.254:8000/api/acciones/";
        analizarService.urlCantidad = "http://192.168.194.254:8000/api/reporte-operaciones/consulta_cliente_accion?";
    }

    @Test
    public void horarioInvalido() throws Exception {
        Orden orden = new Orden();
        orden.setModo("AHORA");
        String fechaOperacionStr = "2023-01-01T18:00:00Z";
        Instant fechaOperacion = Instant.parse(fechaOperacionStr);
        orden.setFechaOperacion(fechaOperacion);
        boolean resultado = analizarService.analizarHorario(orden);
        assertFalse(resultado);
    }

    @Test
    public void horarioValido() throws Exception {
        Orden orden = new Orden();
        orden.setModo("AHORA");
        String fechaOperacionStr = "2023-01-01T17:59:59Z";
        Instant fechaOperacion = Instant.parse(fechaOperacionStr);
        orden.setFechaOperacion(fechaOperacion);
        boolean resultado = analizarService.analizarHorario(orden);
        assertTrue(resultado);
    }

    @Test
    public void clienteInvalido() throws Exception {
        Orden orden = new Orden();
        orden.setCliente(1102L);

        when(catedraService.getClientes()).thenReturn(jsonClientes);

        boolean resultado = analizarService.analizarCliente(orden);
        assertFalse(resultado);
    }

    @Test
    public void clienteValido() throws Exception {
        Orden orden = new Orden();
        orden.setCliente(1113L);
        when(catedraService.getClientes()).thenReturn(jsonClientes);
        boolean resultado = analizarService.analizarCliente(orden);
        assertTrue(resultado);
    }

    @Test
    public void accionInvalida() throws Exception {
        Orden orden = new Orden();
        orden.setAccionId(15L);
        boolean resultado = analizarService.analizarAccion(orden);
        assertFalse(resultado);
    }

    //TODO
    @Test
    public void accionValida() throws Exception {
        Orden orden = new Orden();
        orden.setAccionId(13L);
        boolean resultado = analizarService.analizarAccion(orden);
        assertTrue(resultado);
    }

    @Test
    public void cantidadInvalida() throws Exception {
        Orden orden = new Orden();

        orden.setCliente(1120L);
        orden.setAccionId(13L);
        orden.setAccion("PAM");
        orden.setOperacion("COMPRA");
        orden.setCantidad(10);
        boolean resultado = analizarService.analizarCantidad(orden);
        assertFalse(resultado);
    }

    private void setJsonClientes() {
        ObjectNode cliente1 = mapper.createObjectNode();
        cliente1.put("id", 1113L);
        cliente1.put("nombreApellido", "María Corvalán");
        cliente1.put("empresa", "Happy Soul");

        ArrayNode clientesArray = mapper.createArrayNode();
        clientesArray.add(cliente1);

        ObjectNode objectNode1 = mapper.createObjectNode();
        objectNode1.set("clientes", clientesArray);
        jsonClientes = objectNode1;
    }
}
