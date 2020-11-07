import React, {Component} from 'react';
import {connect} from 'react-redux';
import LoginPage from './LoginPageComponent';
import {logIn} from '../../actions/userActions';

class LoginPageContainer extends Component {
    handleSubmit = payload => {
        return this.props.logIn(payload);
    }

    render() {
        // if (this.props.isLogged)
        //     return (<h1> Zalogowany </h1>);
        // else {
        return (
                <LoginPage onSubmit={this.handleSubmit}/>
        );
    }
}

const mapStateToProps = state => {
    return {
        isLogged: state.user.isLogged
    }
}

const mapDispatchToProps = {
    logIn: logIn
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginPageContainer);