import React from 'react';
import { Route } from 'react-router-dom';
import { routes } from './routes';
import {PrivateRoute} from '../components/PrivateRoute';

export const mappedRoutes = routes.map(route => route.public 
  ? <Route exact path={route.path} component={route.component} key={route.path} />
  : <PrivateRoute exact path={route.path} component={route.component} key={route.path} canAccess={route.canAccess} />)