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
    return axios.patch(`/users/activation/${value}`);
}

export function getUserRequest(value) {
    return axios.get(`/users/password-reset/${value}`);
}

export function sendPasswordResetEmailRequest(payload) {
    return axios.patch('/users/password-reset', JSON.stringify(payload));
}

export function resetPasswordRequest(value, payload, version) {
    return axios.patch(`/users/password-reset/${value}`, JSON.stringify(payload), {
        headers: {
            'If-Match': version
        }
    });
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

export function changeUserPasswordRequest(value, payload, version) {
    return axios.patch(`/users/${value}/password`, JSON.stringify(payload), {
        headers: {
            'If-Match': version
        }
    });
}

export function updateUserDetailsRequest(value, payload, version) {
    return axios.patch(`/users/${value}/details`, JSON.stringify(payload), {
        headers: {
            'If-Match': version
        }
    });
}

export function getOwnDetailsRequest() {
    return axios.get('/users/me');
}

export function changeOwnPasswordRequest(payload, version) {
    return axios.patch('/users/me/password', JSON.stringify(payload), {
        headers: {
            'If-Match': version
        }
    });
}

export function updateOwnDetailsRequest(payload, version) {
    return axios.patch('/users/me/details', JSON.stringify(payload), {
        headers: {
            'If-Match': version
        }
    });
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

export function getOwnAuctionsRequest(values) {
    const {pagination, ...valuesCopy} = values;

    return axios.get('/auctions/selling', {
        params: {
            page: pagination.current - 1,
            ...valuesCopy
        }
    });
}

export function getOwnAuctionDetailsRequest(value) {
    return axios.get(`/auctions/selling/${value}`);
}

export function updateAuctionRequest(value, payload) {
    return axios.patch(`/auctions/selling/${value}`, JSON.stringify(payload));
}

export function getOwnBiddingsRequest(values) {
    const {pagination, ...valuesCopy} = values;

    return axios.get('/auctions/buying', {
        params: {
            page: pagination.current - 1,
            ...valuesCopy
        }
    });
}

export function getOwnBiddingDetailsRequest(value) {
    return axios.get(`/auctions/buying/${value}`);
}

export function placeABidRequest(value, payload) {
    return axios.post(`/auctions/${value}`, JSON.stringify(payload));
}

export function deleteAuctionRequest(value) {
    return axios.delete(`/auctions/${value}`);
}