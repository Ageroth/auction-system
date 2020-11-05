import React, {Suspense} from 'react';
import ReactDOM from 'react-dom';
import App from './App'
import './utils/i18n';
import {Provider} from "react-redux";
import {PersistGate} from 'redux-persist/integration/react';
import {persistor, store} from "./store";

ReactDOM.render(
    <Suspense fallback={null}>
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                <App/>
            </PersistGate>
        </Provider>
    </Suspense>,
    document.getElementById('root'));