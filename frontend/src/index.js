import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
import App from './App'
import { Provider } from "react-redux";
import { PersistGate } from 'redux-persist/integration/react';
import { persistor, store } from "./store";
import './utils/i18n';
import Interceptor from './utils/interceptor'
Interceptor.interceptor(store);

ReactDOM.render(
    <Suspense fallback={null}>
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                <App/>
            </PersistGate>
        </Provider>
    </Suspense>,
    document.getElementById('root'));