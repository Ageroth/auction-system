import axios from 'axios'

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
    return axios.patch(`/users/me/activation/${value}`);
}

export function sendPasswordResetEmailRequest(payload) {
    return axios.patch('/users/me/password-reset', JSON.stringify(payload));
}

export function resetPasswordRequest(value, payload) {
    return axios.patch(`/users/me/password-reset/${value}`, JSON.stringify(payload));
}

export function getUsersRequest(values) {
    const {pagination, ...valuesCopy} = values;

    return axios.get('/users', {
        params: {
            page: pagination.current - 1,
            ...valuesCopy
        }
    });
}

export function addUserRequest(payload) {
    return axios.post('/users', JSON.stringify(payload));
}

export function getUserDetailsRequest(value) {
    return axios.get(`/users/${value}`);
}

export function changeUserPasswordRequest(value, payload) {
    return axios.patch(`/users/${value}/password`, JSON.stringify(payload));
}

export function updateUserDetailsRequest(value, payload) {
    return axios.put(`/users/${value}/details`, JSON.stringify(payload));
}

export function getMyDetailsRequest() {
    return axios.get('/users/me');
}

export function changeOwnPasswordRequest(payload) {
    return axios.patch('/users/me/password', JSON.stringify(payload));
}

export function updateOwnDetailsRequest(payload) {
    return axios.put('/users/me/details', JSON.stringify(payload));
}

export function getAllAccessLevelsRequest() {
    return axios.get('/access-levels');
}

export function getAuctionsRequest(values) {
    const {pagination, ...valuesCopy} = values;

    return axios.get('/auctions', {
        params: {
            page: pagination.current - 1,
            ...valuesCopy
        }
    });
}

export function getAuctionDetailsRequest(value) {
    return axios.get(`/auctions/${value}`);
}

export function addAuctionRequest(payload) {
    return axios.post('/auctions', payload);
}

export function updateAuctionRequest(value, payload) {
    return axios.patch(`/auctions/${value}`, JSON.stringify(payload));
}