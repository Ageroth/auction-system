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
        username: value
      }
    });
}

export function checkEmailAvailabilityRequest(value) {
    return axios.get('/users/email-availability', {
        params: {
        email: value
      }
    });
}

export function activateUserRequest(value) {
    return axios.post('/users/me/activation' + value);
}

export function sendPasswordResetEmailRequest(payload) {
    return axios.post('/users/me/password-reset', JSON.stringify(payload));
}

export function resetPasswordRequest(value, payload) {
    return axios.post('/users/me/password-reset' + value, JSON.stringify(payload));
}

export function getUsersRequest(values) {
    return axios.get('/users', {
        headers: {
          Authorization: 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZ2Vyb3RoIiwiaWF0IjoxNjA1MzgxNDAxLCJleHAiOjE2MDUzODIzMDF9.lBtU9UTePXPXU0dCnCC-y8ujqFH3gRUTTpwPJxQ6IRQDGmVwDgyoRVMCcbVg2VGE68JN0907Er_IVCew8okAwA'
        }
       }, {
           params: {
               page: values.current - 1
           }
       });
}