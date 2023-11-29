package ar.edu.um.programacion2.martin.service.mapper;

import ar.edu.um.programacion2.martin.domain.Orden;
import ar.edu.um.programacion2.martin.service.dto.OrdenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orden} and its DTO {@link OrdenDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrdenMapper extends EntityMapper<OrdenDTO, Orden> {}
