import React, {Suspense} from 'react';
import ReactDOM from 'react-dom';
import App from './App'
import {Provider} from "react-redux";
import {PersistGate} from 'redux-persist/integration/react';
import {persistor, store} from "./store";
import {ConfigProvider} from 'antd';
import Interceptor from './utils/interceptor'
import i18n from "./utils/i18n";
import plPL from 'antd/lib/locale/pl_PL';
import enGB from 'antd/lib/locale/en_GB';

Interceptor.interceptor(store);

ReactDOM.render(
    <Suspense fallback={null}>
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                <ConfigProvider
                    locale={(i18n.language ? i18n.language : window.localStorage.i18nextLng) === 'pl' ? plPL : enGB}>
                    <App/>
                </ConfigProvider>
            </PersistGate>
        </Provider>
    </Suspense>,
    document.getElementById('root')
)
;