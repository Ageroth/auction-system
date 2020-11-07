import React from 'react';
import {Route, BrowserRouter, Switch} from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import SignupPage from './pages/SignupPage'
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const App = () => {
    return (
        <div className="App">
            <BrowserRouter>
                <Switch>
                    <Route exact path="/login">
                        <LoginPage/>
                    </Route>
                    <Route exact path="/signup">
                        <SignupPage/>
                    </Route>
                </Switch>
            </BrowserRouter>
            <ToastContainer />
        </div>
    );
}

export default App;