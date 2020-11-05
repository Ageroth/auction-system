import React, {Component} from 'react';
import LoginPage from "./LoginPageComponent";
import {logIn} from "../../actions/userActions";
import {connect} from "react-redux";

class LoginPageContainer extends Component {
    handleLogin = values => {
        const payload = Object.assign({}, values);
        return this.props.logIn(payload);
    };

    render() {
        // if (this.props.isLogged)
        //     return (<h1> Zalogowany </h1>);
        // else {
        return (
            <div className="App">
                <LoginPage onSubmit={this.handleLogin}/>
            </div>
        );}
}

const mapStateToProps = state => {
    return {
        isLogged: state.user.isLogged
    }
};

const mapDispatchToProps = {
    logIn: logIn
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginPageContainer);