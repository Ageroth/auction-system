import React from 'react';
import {Router, Switch} from 'react-router-dom';
import {history} from './utils/history'
import {ToastContainer} from 'react-toastify';
import {mappedRoutes} from './utils/mappedRoutes';
import 'react-toastify/dist/ReactToastify.css';


const App = () => {
    return (
        <>
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