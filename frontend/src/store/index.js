import {applyMiddleware, createStore} from 'redux';
import {persistReducer, persistStore} from 'redux-persist';
import thunk from 'redux-thunk';
import {composeWithDevTools} from 'redux-devtools-extension'

import rootReducer from './combinedReducers';
import storage from 'redux-persist/lib/storage';

const persistConfig = {
    key: 'root',
    storage,
    whitelist: ['settings', 'user']
};


const persistedReducer = persistReducer(persistConfig, rootReducer);
const composedEnhancer = composeWithDevTools(applyMiddleware(thunk))

const store = createStore(persistedReducer, composedEnhancer);

const persistor = persistStore(store);

export {store, persistor};