package ar.edu.um.programacion2.martin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ColaAhoraService {

    private final Logger log = LoggerFactory.getLogger(ColaAhoraService.class);
}
