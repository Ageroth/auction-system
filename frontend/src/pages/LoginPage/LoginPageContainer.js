import React, {Component} from 'react';
import LoginPage from "./LoginPageComponent";
import {loginUser} from "../../actions/userActions";
import {connect} from "react-redux";

class LoginPageContainer extends Component {
    handleLogin = values => {
        const loginRequest = Object.assign({}, values);
        return this.props.loginUser(loginRequest);
    };

    render() {
        const {isLogged} = this.props.isLogged
        if (isLogged)
            return (<h1> Zalogowany </h1>);
        else {
        return (
            <div className="App">
                <LoginPage onSubmit={this.handleLogin}/>
            </div>
        );}
    }
}

const mapStateToProps = state => ({
    isLogged: state.user.isLogged
});

const mapDispatchToProps = {
    loginUser
};

export default connect(mapStateToProps, mapDispatchToProps)(LoginPageContainer);