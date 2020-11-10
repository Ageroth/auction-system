import React from 'react';
import {Route, BrowserRouter, Switch} from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage'
import ActivationPage from './pages/ActivationPage';
import PasswordResetPage from './pages/PasswordResetPage'
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const App = () => {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route exact path="/login" component={LoginPage} />
                    <Route exact path="/signup" component={SignupPage} />
                    <Route exact path="/activation/:activationCode" component={ActivationPage} />
                    <Route exact path="/password_reset" component={PasswordResetPage} />
                    <Route exact path="/password_reset/:passwordResetCode" component={PasswordResetPage} />
                </Switch>
            </BrowserRouter>
            <ToastContainer/>
        </div>
    );
}

export default App;