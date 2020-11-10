import React, {Component} from 'react';
import SignupPage from './SignupPageComponent';
import {signUpRequest} from '../../utils/api';


class SignupPageContainer extends Component {
    handleSignup = payload => {
        return signUpRequest(payload);
    }

    render() {
        // if (this.props.isLogged)
        //     return (<h1> Zalogowany </h1>);
        // else {
        return (
            <SignupPage onSubmit={this.handleSignup}/>
        );
    }
}

export default SignupPageContainer;