import axios from 'axios'
import { logOut } from '../actions/userActions';

const interceptor = (store) => {
    axios.interceptors.request.use(
        (config) => {
            const token = store.getState().user.token;
           
            if (token) {
                config.headers = { 
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            } else {
                config.headers = { 
                    'Content-Type': 'application/json'
                }
            }
            
            config.baseURL = 'http://localhost:8080/api';
            
            return config;
        },
        (error) => {
          return Promise.reject(error);
        }
    );
    axios.interceptors.response.use(
        (next) => {
            return Promise.resolve(next);
        },
        (error) => {
            const isLoggedIn = store.getState().user.isLoggedIn;

            if (error.response.status === 401 && isLoggedIn) {
                store.dispatch(logOut());
            }

            return Promise.reject(error);
        }
    )
}

export default {interceptor};