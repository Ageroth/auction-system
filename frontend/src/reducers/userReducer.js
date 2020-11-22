import {CHANGE_CURRENT_ROLE, LOGIN_USER, LOGOUT_USER} from '../actions/types';
import allroles from '../utils/allroles'

const initialState = {
    isLoggedIn: false,
    username: null,
    token: null,
    roles: null,
    currentRole: null
};

export default function userReducer(state = initialState, action) {
    switch (action.type) {
        case LOGIN_USER:
            const slicedRoles = action.payload.roles.map(s => s.slice(5));
            const userRoles = Object.keys(allroles).filter(r =>
                slicedRoles.includes(r)
            );
            return {
                ...state,
                isLoggedIn: true,
                username: action.payload.username,
                token: action.payload.token,
                roles: userRoles,
                currentRole: userRoles[userRoles.length - 1]
            };
        case LOGOUT_USER:
            return initialState;
        case CHANGE_CURRENT_ROLE:
            return {
                ...state,
                currentRole: action.payload
            }
        default:
            return state;
    }
};