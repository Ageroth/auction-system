import React from 'react';
import {Router, Switch} from 'react-router-dom';
import {history} from './utils/history'
import {ToastContainer} from 'react-toastify';
import {mappedRoutes} from './utils/mappedRoutes';
import 'react-toastify/dist/ReactToastify.css';
import {Helmet} from 'react-helmet'

const App = () => {
    return (
        <>
            <Helmet>
                <title>Auction system</title>
            </Helmet>
            <Router history={history}>
                <Switch>
                    {mappedRoutes}
                </Switch>
            </Router>
            <ToastContainer/>
        </>
    );
}

export default App;