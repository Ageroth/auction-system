import React, { Component } from 'react';
import { Router, Route, Switch } from 'react-router-dom';
import routes from '../../utils/routes';
import PrivateRoute from './PrivateRoute';
import {history} from '../../utils/history'

class MyRouter extends Component {
  render() {
    return (
      <Router history={history}>
          <Route
            render={({location, match}) => (
                <Switch location={location}>
                    {routes.map(route => route.public 
                        ? <Route key={route.path} exact {...route} />
                        : <PrivateRoute key={route.path} exact {...route} history={history} />
                    )}
                </Switch>
            )}
          />
      </Router>
    );
  }
}

export default MyRouter;