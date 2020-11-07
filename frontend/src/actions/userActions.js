import {logInRequest} from '../utils/api'
import {LOGIN_USER, LOGOUT_USER} from "./types";
import {toast} from 'react-toastify';

const logIn = (payload) => async dispatch => {
    try {
        const response = await logInRequest(payload)
        dispatch({
            type: LOGIN_USER,
            payload: response.data
        })
    } catch (e) {
        toast.error(e.response.data.message, {
            position: "bottom-right",
            autoClose: false,
            closeOnClick: true
            });
    }
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