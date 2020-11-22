import {logInRequest} from '../utils/api'
import {CHANGE_CURRENT_ROLE, LOGIN_USER, LOGOUT_USER} from "./types";

export const logIn = (payload) => async dispatch => {
    const response = await logInRequest(payload)
    dispatch({
        type: LOGIN_USER,
        payload: response.data
    })
}

export const logOut = () => {
    return {
        type: LOGOUT_USER
    };
}

export const changeCurrentRole = (role) => {
    return {
        type: CHANGE_CURRENT_ROLE,
        payload: role
    };
}