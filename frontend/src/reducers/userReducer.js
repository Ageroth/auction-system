import {ACTIONS} from '../actions/userActions'

const initialState = {
    isLogged: false,
    username: null,
    token: null,
    roles: null
};

export default function userReducer(state = initialState, action) {
    switch (action.type) {
        case ACTIONS.LOGIN_USER:
            return {
                ...state,
                isLogged: true,
                username: action.payload.username,
                token: action.payload.token,
                roles: action.payload.roles
            }
        default:
            return state
    }
};