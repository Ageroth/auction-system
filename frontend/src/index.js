import React, {Suspense} from 'react';
import ReactDOM from 'react-dom';
import App from './App'
import './utils/i18n';
import {Provider} from "react-redux";
import {PersistGate} from 'redux-persist/integration/react';
import {persistor, store} from "./store";
import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8080/api';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.patch['Content-Type'] = 'application/json';

ReactDOM.render(
    <Suspense fallback={null}>
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                <App/>
            </PersistGate>
        </Provider>
    </Suspense>,
    document.getElementById('root'));