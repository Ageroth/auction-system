import React from 'react';
import {Redirect, Route} from 'react-router-dom';
import {useSelector} from 'react-redux'

const PrivateRoute = ({component: Component, canAccess, ...rest}) => {
    const isLoggedIn = useSelector(state => state.user.isLoggedIn);
    const currentRole = useSelector(state => state.user.currentRole);

    const haveAccessToRoute = () => {
        let result = false;

        if (canAccess) {
            if (canAccess.includes(currentRole)) result = true;
        } else result = true;

        return result;
    }

    return (
        <Route {...rest} render={props => (
            isLoggedIn ? (
                haveAccessToRoute() ? <Component {...props} /> : <Redirect to="/"/>
            ) : <Redirect to="/login"/>
        )}/>
    );
}

export default PrivateRoute