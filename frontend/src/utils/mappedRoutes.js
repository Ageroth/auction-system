import React from 'react';
import {Route} from 'react-router-dom';
import {routes} from './routes';
import {PrivateRoute} from '../components/PrivateRoute';

export const mappedRoutes = routes.map(route => route.public
    ? <Route key={route.path} exact path={route.path} component={route.component}/>
    : <PrivateRoute key={route.path} exact path={route.path} component={route.component} canAccess={route.canAccess}/>)