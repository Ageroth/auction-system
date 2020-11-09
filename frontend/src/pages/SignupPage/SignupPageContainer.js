import React, {Component} from 'react';
import SignupPage from './SignupPageComponent';
import {signUpRequest} from '../../utils/api';


class SignupPageContainer extends Component {
    handleSubmit = payload => {
        return signUpRequest(payload);
    }

    render() {
        // if (this.props.isLogged)
        //     return (<h1> Zalogowany </h1>);
        // else {
        return (
                <SignupPage onSubmit={this.handleSubmit}/>
        );
    }
}

export default SignupPageContainer;