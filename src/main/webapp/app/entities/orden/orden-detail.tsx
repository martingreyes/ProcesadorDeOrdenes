import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './orden.reducer';

export const OrdenDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ordenEntity = useAppSelector(state => state.orden.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ordenDetailsHeading">
          <Translate contentKey="procesadorDeOrdenesApp.orden.detail.title">Orden</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.id}</dd>
          <dt>
            <span id="cliente">
              <Translate contentKey="procesadorDeOrdenesApp.orden.cliente">Cliente</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.cliente}</dd>
          <dt>
            <span id="accionId">
              <Translate contentKey="procesadorDeOrdenesApp.orden.accionId">Accion Id</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.accionId}</dd>
          <dt>
            <span id="accion">
              <Translate contentKey="procesadorDeOrdenesApp.orden.accion">Accion</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.accion}</dd>
          <dt>
            <span id="operacion">
              <Translate contentKey="procesadorDeOrdenesApp.orden.operacion">Operacion</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.operacion}</dd>
          <dt>
            <span id="precio">
              <Translate contentKey="procesadorDeOrdenesApp.orden.precio">Precio</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.precio}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="procesadorDeOrdenesApp.orden.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.cantidad}</dd>
          <dt>
            <span id="fechaOperacion">
              <Translate contentKey="procesadorDeOrdenesApp.orden.fechaOperacion">Fecha Operacion</Translate>
            </span>
          </dt>
          <dd>
            {ordenEntity.fechaOperacion ? <TextFormat value={ordenEntity.fechaOperacion} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="modo">
              <Translate contentKey="procesadorDeOrdenesApp.orden.modo">Modo</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.modo}</dd>
          <dt>
            <span id="analisis">
              <Translate contentKey="procesadorDeOrdenesApp.orden.analisis">Analisis</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.analisis ? 'true' : 'false'}</dd>
          <dt>
            <span id="procesamiento">
              <Translate contentKey="procesadorDeOrdenesApp.orden.procesamiento">Procesamiento</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.procesamiento ? 'true' : 'false'}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="procesadorDeOrdenesApp.orden.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{ordenEntity.descripcion}</dd>
        </dl>
        <Button tag={Link} to="/orden" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/orden/${ordenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrdenDetail;
