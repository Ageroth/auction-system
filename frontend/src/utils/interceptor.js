import axios from 'axios'
import {logOut} from '../actions/userActions';
import {history} from "./history";

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

            if (error.response.status === 403 && isLoggedIn) {
                history.push("/403")
            }

            if (error.response.status === 404) {
                history.push("/404")
            }

            if (error.response.status === 500) {
                history.push("/500")
            }

            return Promise.reject(error);
        }
    )
}

export default {interceptor};