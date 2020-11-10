import {LOGIN_USER, LOGOUT_USER} from "../actions/types";
import ALLROLES from '../utils/allroles'

const initialState = {
    isLogged: false,
    username: null,
    token: null,
    roles: null
};

export default function userReducer(state = initialState, action) {
    switch (action.type) {
        case LOGIN_USER:
            const slicedRoles = action.payload.roles.map(s => s.slice(5));
            const userRoles = Object.keys(ALLROLES).filter(r =>
                slicedRoles.includes(r)
            );
            return {
                ...state,
                isLogged: true,
                username: action.payload.username,
                token: action.payload.token,
                roles: userRoles
            }
        case LOGOUT_USER:
            return initialState;
        default:
            return state
    }
};