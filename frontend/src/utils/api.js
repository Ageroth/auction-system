import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8080/api';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.patch['Content-Type'] = 'application/json';
axios.defaults.headers.get['Content-Type'] = 'application/json';
axios.defaults.headers.common['Authorization'] = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbWMiLCJpYXQiOjE2MDU0ODI4ODAsImV4cCI6MTYwNTQ5MTg4MH0.pd7srkbEqOM7Bet-K_PkrHTl8ycuBAe-thLoNtMWzwuzMkKAv5Jj9RixwKH70ZoICzJWMuBlD09upucmx3hsrA';

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
    let activationStatus;
    values.activated ?  activationStatus = values.activated[0] : activationStatus = null;

    return axios.get('/users', {
        params: {
            page: values.pagination.current - 1,
            status: activationStatus,
            sortField: values.sortField,
            order: values.order
        }
    });
}