import axios from 'axios'

export const ACTIONS = {
    LOGIN_USER: 'USER_LOGIN_USER',
    LOGOUT_USER: 'USER_LOGOUT_USER',
};

const loginUser = (loginRequest) => async dispatch => {
    const response = await axios.post(`/auth/login`, JSON.stringify(loginRequest))
    dispatch({
        type: ACTIONS.LOGIN_USER,
        payload: response.data
    })
}

export {
    loginUser
};