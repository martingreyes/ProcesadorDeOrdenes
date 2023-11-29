package ar.edu.um.programacion2.martin.service;

import ar.edu.um.programacion2.martin.domain.Orden;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ColaAhoraService {

    private final Logger log = LoggerFactory.getLogger(ColaAhoraService.class);

    private Queue<Orden> cola = new LinkedList<>();

    // Método para añadir elementos a la cola
    public void agregarOrden(Orden orden) {
        cola.add(orden);
    }

    // Método para quitar y devolver el primer elemento de la cola
    public Orden quitarOrden() {
        return cola.poll();
    }

    // Método para ver el primer elemento de la cola sin quitarlo
    public Orden verSiguienteOrden() {
        return cola.peek();
    }

    // Método para verificar si la cola está vacía
    public boolean estaVacia() {
        return cola.isEmpty();
    }

    // Método para obtener el tamaño de la cola
    public int tamanoCola() {
        return cola.size();
    }

    public List<Orden> obtenerElementosDeCola() {
        List<Orden> listaOrdenes = new ArrayList<>(cola);
        return listaOrdenes;
    }
}
