import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8080/api';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.patch['Content-Type'] = 'application/json';

export function logInRequest(payload) {
    return axios.post(`/auth/login`, JSON.stringify(payload));
}