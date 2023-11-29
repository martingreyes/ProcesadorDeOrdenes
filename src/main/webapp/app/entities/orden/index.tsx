import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Orden from './orden';
import OrdenDetail from './orden-detail';
import OrdenUpdate from './orden-update';
import OrdenDeleteDialog from './orden-delete-dialog';

const OrdenRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Orden />} />
    <Route path="new" element={<OrdenUpdate />} />
    <Route path=":id">
      <Route index element={<OrdenDetail />} />
      <Route path="edit" element={<OrdenUpdate />} />
      <Route path="delete" element={<OrdenDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OrdenRoutes;
