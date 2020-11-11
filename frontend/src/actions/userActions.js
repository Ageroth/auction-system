import {logInRequest} from '../utils/api'
import {LOGIN_USER, LOGOUT_USER} from "./types";


const logIn = (payload) => async dispatch => {
        const response = await logInRequest(payload)
        dispatch({
            type: LOGIN_USER,
            payload: response.data
        })
}

const logOut = () => async dispatch => {
    dispatch({
        type: LOGOUT_USER
    });
}

export {
    logIn,
    logOut
};