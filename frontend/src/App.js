import React from 'react';
import {Route, BrowserRouter, Switch} from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage'
import ActivationPage from './pages/ActivationPage';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const App = () => {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route exact path="/login" component={LoginPage} />
                    <Route exact path="/signup" component={SignupPage} />
                    <Route exact path="/activate/:id" component={ActivationPage} />
                </Switch>
            </BrowserRouter>
            <ToastContainer/>
        </div>
    );
}

export default App;