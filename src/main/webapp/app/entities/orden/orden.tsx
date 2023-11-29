import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrden } from 'app/shared/model/orden.model';
import { getEntities } from './orden.reducer';

export const Orden = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ordenList = useAppSelector(state => state.orden.entities);
  const loading = useAppSelector(state => state.orden.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="orden-heading" data-cy="OrdenHeading">
        <Translate contentKey="procesadorDeOrdenesApp.orden.home.title">Ordens</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="procesadorDeOrdenesApp.orden.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/orden/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="procesadorDeOrdenesApp.orden.home.createLabel">Create new Orden</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ordenList && ordenList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.cliente">Cliente</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.accionId">Accion Id</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.accion">Accion</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.operacion">Operacion</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.precio">Precio</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.cantidad">Cantidad</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.fechaOperacion">Fecha Operacion</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.modo">Modo</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.analisis">Analisis</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.procesamiento">Procesamiento</Translate>
                </th>
                <th>
                  <Translate contentKey="procesadorDeOrdenesApp.orden.descripcion">Descripcion</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ordenList.map((orden, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/orden/${orden.id}`} color="link" size="sm">
                      {orden.id}
                    </Button>
                  </td>
                  <td>{orden.cliente}</td>
                  <td>{orden.accionId}</td>
                  <td>{orden.accion}</td>
                  <td>{orden.operacion}</td>
                  <td>{orden.precio}</td>
                  <td>{orden.cantidad}</td>
                  <td>{orden.fechaOperacion ? <TextFormat type="date" value={orden.fechaOperacion} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{orden.modo}</td>
                  <td>{orden.analisis ? 'true' : 'false'}</td>
                  <td>{orden.procesamiento ? 'true' : 'false'}</td>
                  <td>{orden.descripcion}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/orden/${orden.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/orden/${orden.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/orden/${orden.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="procesadorDeOrdenesApp.orden.home.notFound">No Ordens found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Orden;
