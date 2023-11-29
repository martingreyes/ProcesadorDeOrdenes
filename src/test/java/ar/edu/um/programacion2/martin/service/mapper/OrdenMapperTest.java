package ar.edu.um.programacion2.martin.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrdenMapperTest {

    private OrdenMapper ordenMapper;

    @BeforeEach
    public void setUp() {
        ordenMapper = new OrdenMapperImpl();
    }
}
