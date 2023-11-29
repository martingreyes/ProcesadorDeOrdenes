package ar.edu.um.programacion2.martin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MainService {

    @Autowired
    ObtenerService obtenerService;

    @Autowired
    AnalizarService analizarService;

    @Autowired
    ProcesarService procesarService;

    @Autowired
    ReportarService reportarService;

    private final Logger log = LoggerFactory.getLogger(MainService.class);

    //@Scheduled(cron = "0/8 * * * * ?")
    //@Scheduled(cron = "0/30 9-17 * * * ?")
    public void Serve() {
        obtenerService.obtenerOrdenes();
        analizarService.analizarOrdenes("AHORA");
        procesarService.procesarOrdenes("AHORA");
        reportarService.reportarOrdenes("AHORA");
    }

    //@Scheduled(cron = "0/21 * * * * ?")
    //@Scheduled(cron = "0 0 9 * * ?")
    public void ServePrincipioDia() {
        obtenerService.obtenerOrdenes();
        analizarService.analizarOrdenes("PRINCIPIODIA");
        procesarService.procesarOrdenes("PRINCIPIODIA");
        reportarService.reportarOrdenes("PRINCIPIODIA");
    }

    //@Scheduled(cron = "0/29 * * * * ?")
    //@Scheduled(cron = "0 0 18 * * ?")
    public void ServeFinDia() {
        obtenerService.obtenerOrdenes();
        analizarService.analizarOrdenes("FINDIA");
        procesarService.procesarOrdenes("FINDIA");
        reportarService.reportarOrdenes("FINDIA");
    }
}
