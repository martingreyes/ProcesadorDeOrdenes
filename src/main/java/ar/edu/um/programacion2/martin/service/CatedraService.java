package ar.edu.um.programacion2.martin.service;

import ar.edu.um.programacion2.martin.domain.Orden;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CatedraService {

    private final Logger log = LoggerFactory.getLogger(CatedraService.class);

    @Value("${miapp.url-ordenes-local}")
    private String urlOrdenesLocal;

    @Value("${miapp.url-ordenes}")
    private String urlOrdenes;

    @Value("${miapp.url-clientes}")
    public String urlClientes;

    @Value("${miapp.url-acciones}")
    public String urlAcciones;

    @Value("${miapp.url-cantidad}")
    public String urlCantidad;

    @Value("${miapp.url-ultimovalor}")
    private String urlUltimoValor;

    @Value("${miapp.url-reportar}")
    private String urlReportar;

    @Value("${miapp.token}")
    public String token;

    public HttpResponse<String> getOrdenes() {
        String uri = urlOrdenesLocal;
        //String uri = urlOrdenes;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).header("Authorization", "Bearer " + token).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println("Response status code: " + response.statusCode());
            //System.out.println("Response body: " + response.body());

            log.info("GET " + uri + " : " + response.statusCode());

            return response;
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            return null;
        }
    }

    public JsonNode getClientes() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlClientes)).header("Authorization", "Bearer " + token).build();
        JsonNode clientesNode;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("GET " + urlClientes + " : " + response.statusCode());
            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            clientesNode = jsonNode.get("clientes");
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            clientesNode = null;
        }
        return clientesNode;
    }

    public JsonNode getAcciones() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlAcciones)).header("Authorization", "Bearer " + token).build();
        JsonNode accionesNode;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("GET " + urlAcciones + " : " + response.statusCode());
            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            accionesNode = jsonNode.get("acciones");
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            accionesNode = null;
        }
        return accionesNode;
    }

    public JsonNode getCantidad(Orden orden) {
        JsonNode cantidadActualNode;
        String url = urlCantidad + "clienteId=" + orden.getCliente() + "&accionId=" + orden.getAccionId();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Authorization", "Bearer " + token).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("GET " + url + " : " + response.statusCode());
            log.info(response.body());
            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            cantidadActualNode = jsonNode.get("cantidadActual");
        } catch (IOException | InterruptedException e) {
            cantidadActualNode = null;
        }
        return cantidadActualNode;
    }

    public JsonNode getPrecio(Orden orden) {
        JsonNode ultimoValorNode;
        String simbolo = orden.getAccion();
        String uri = urlUltimoValor + simbolo;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).header("Authorization", "Bearer " + token).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("GET " + urlUltimoValor + simbolo + " : " + response.statusCode());
            String jsonResponse = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            ultimoValorNode = jsonNode.get("ultimoValor");
        } catch (IOException | InterruptedException e) {
            ultimoValorNode = null;
            //e.printStackTrace();
        }

        return ultimoValorNode;
    }

    public HttpResponse<String> postOrdenes(Map<String, ArrayNode> data) {
        //! Para construir el body del Json a partir de Map<String, List<OrdenReporte>>
        ObjectMapper mapper = new ObjectMapper();
        byte[] jsonBytes = new byte[0];

        try {
            jsonBytes = mapper.writeValueAsString(data).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        BodyPublisher body = BodyPublishers.ofByteArray(jsonBytes);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create(urlReportar))
            .header("Authorization", "Bearer " + token)
            .header("Content-Type", "application/json")
            .POST(body)
            .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println("Response status code: " + response.statusCode());
            //System.out.println("Response body: " + response.body());

            return response;
        } catch (IOException | InterruptedException e) {
            //e.printStackTrace();
            return null;
        }
    }
}
