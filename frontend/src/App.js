import React from 'react';
import {Route, Router, Switch} from 'react-router-dom';
import history from './utils/history'
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage'
import ActivationPage from './pages/ActivationPage';
import PasswordResetPage from './pages/PasswordResetPage'
import HomePage from './pages/HomePage'
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const App = () => {
    return (
        <div className="App">
            <Router history={history}>
                <Switch>
                    <Route exact path="/login" component={LoginPage} />
                    <Route exact path="/signup" component={SignupPage} />
                    <Route exact path="/activation/:activationCode" component={ActivationPage} />
                    <Route exact path="/password_reset" component={PasswordResetPage} />
                    <Route exact path="/password_reset/:passwordResetCode" component={PasswordResetPage} />
                    <Route exact path="/" component={HomePage} />
                </Switch>
            </Router>
            <ToastContainer/>
        </div>
    );
}

export default App;