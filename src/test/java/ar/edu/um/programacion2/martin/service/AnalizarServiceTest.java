package ar.edu.um.programacion2.martin.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import ar.edu.um.programacion2.martin.domain.Orden;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Value("${miapp.url-clientes}")
    private String urlClientes;

    @Value("${miapp.url-acciones}")
    private String urlAcciones;

    @Value("${miapp.url-cantidad}")
    private String urlCantidad;

    @Value("${miapp.token}")
    private String token;

    private JsonNode jsonClientes;
    private JsonNode jsonAcciones;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        setJsonClientes();
        setJsonAcciones();
        catedraService.token =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtYXJ0aW5yZXllcyIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE3Mjk2ODg3ODV9.6yeagJAm9m8cV_gjHu8e6UPlTkdS8iqkX4_GlvwKrqshcpIqs6URQFyBAYI2uEKgiW6Zvn5E5x6HEhPDkecQVg";
        catedraService.urlClientes = "http://192.168.194.254:8000/api/clientes/";
        catedraService.urlAcciones = "http://192.168.194.254:8000/api/acciones/";
        catedraService.urlCantidad = "http://192.168.194.254:8000/api/reporte-operaciones/consulta_cliente_accion?";
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
        when(catedraService.getAcciones()).thenReturn(jsonAcciones);
        boolean resultado = analizarService.analizarAccion(orden);
        assertFalse(resultado);
    }

    @Test
    public void accionValida() throws Exception {
        Orden orden = new Orden();
        orden.setAccionId(2L);
        when(catedraService.getAcciones()).thenReturn(jsonAcciones);
        boolean resultado = analizarService.analizarAccion(orden);
        assertTrue(resultado);
    }

    @Test
    public void cantidadInvalida() throws Exception {
        Orden orden = new Orden();
        orden.setCliente(1113L);
        orden.setAccionId(13L);
        orden.setCantidad(101);
        when(catedraService.getCantidad(orden)).thenReturn(JsonNodeFactory.instance.numberNode(100));
        boolean resultado = analizarService.analizarCantidad(orden);
        assertFalse(resultado);
    }

    @Test
    public void cantidadValida() throws Exception {
        Orden orden = new Orden();
        orden.setCliente(1113L);
        orden.setAccionId(13L);
        orden.setCantidad(99);
        when(catedraService.getCantidad(orden)).thenReturn(JsonNodeFactory.instance.numberNode(100));
        boolean resultado = analizarService.analizarCantidad(orden);
        assertTrue(resultado);
    }

    private void setJsonClientes() {
        ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
        ObjectNode cliente = JsonNodeFactory.instance.objectNode();
        cliente.put("id", 1113);
        cliente.put("nombreApellido", "María Corvalán");
        cliente.put("empresa", "Happy Soul");
        jsonArray.add(cliente);
        ObjectNode cliente2 = JsonNodeFactory.instance.objectNode();
        cliente2.put("id", 1114);
        cliente2.put("nombreApellido", "Ricardo Tapia");
        cliente2.put("empresa", "Salud Zen");
        jsonArray.add(cliente2);
        JsonNode jsonNode = jsonArray;
        jsonClientes = jsonNode;
    }

    private void setJsonAcciones() {
        ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
        ObjectNode accion = JsonNodeFactory.instance.objectNode();
        accion.put("id", 1);
        accion.put("codigo", "AAPL");
        accion.put("empresa", "Apple Inc.");
        jsonArray.add(accion);
        ObjectNode accion2 = JsonNodeFactory.instance.objectNode();
        accion2.put("id", 2);
        accion2.put("codigo", "GOOGL");
        accion2.put("empresa", "Alphabet Inc. (google)");
        jsonArray.add(accion2);
        JsonNode jsonNode = jsonArray;
        jsonAcciones = jsonNode;
    }
}
