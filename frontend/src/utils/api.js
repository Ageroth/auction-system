import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8080/api';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.patch['Content-Type'] = 'application/json';
axios.defaults.headers.get['Content-Type'] = 'application/json';

export function logInRequest(payload) {
    return axios.post(`/auth/login`, JSON.stringify(payload));
}

export function signUpRequest(payload) {
    return axios.post(`/users/me`, JSON.stringify(payload));
}

export function checkUsernameAvailabilityRequest(value) {
    return axios.get('/users/username-availability', {
        params: {
        username : value
      }
    })
}

export function checkEmailAvailabilityRequest(value) {
    return axios.get('/users/email-availability', {
        params: {
        email : value
      }
    })
}