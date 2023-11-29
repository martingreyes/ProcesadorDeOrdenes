import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrden } from 'app/shared/model/orden.model';
import { getEntity, updateEntity, createEntity, reset } from './orden.reducer';

export const OrdenUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ordenEntity = useAppSelector(state => state.orden.entity);
  const loading = useAppSelector(state => state.orden.loading);
  const updating = useAppSelector(state => state.orden.updating);
  const updateSuccess = useAppSelector(state => state.orden.updateSuccess);

  const handleClose = () => {
    navigate('/orden');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fechaOperacion = convertDateTimeToServer(values.fechaOperacion);

    const entity = {
      ...ordenEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaOperacion: displayDefaultDateTime(),
        }
      : {
          ...ordenEntity,
          fechaOperacion: convertDateTimeFromServer(ordenEntity.fechaOperacion),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="procesadorDeOrdenesApp.orden.home.createOrEditLabel" data-cy="OrdenCreateUpdateHeading">
            <Translate contentKey="procesadorDeOrdenesApp.orden.home.createOrEditLabel">Create or edit a Orden</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="orden-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.cliente')}
                id="orden-cliente"
                name="cliente"
                data-cy="cliente"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.accionId')}
                id="orden-accionId"
                name="accionId"
                data-cy="accionId"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.accion')}
                id="orden-accion"
                name="accion"
                data-cy="accion"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.operacion')}
                id="orden-operacion"
                name="operacion"
                data-cy="operacion"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.precio')}
                id="orden-precio"
                name="precio"
                data-cy="precio"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.cantidad')}
                id="orden-cantidad"
                name="cantidad"
                data-cy="cantidad"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.fechaOperacion')}
                id="orden-fechaOperacion"
                name="fechaOperacion"
                data-cy="fechaOperacion"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.modo')}
                id="orden-modo"
                name="modo"
                data-cy="modo"
                type="text"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.analisis')}
                id="orden-analisis"
                name="analisis"
                data-cy="analisis"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.procesamiento')}
                id="orden-procesamiento"
                name="procesamiento"
                data-cy="procesamiento"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('procesadorDeOrdenesApp.orden.descripcion')}
                id="orden-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/orden" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default OrdenUpdate;
