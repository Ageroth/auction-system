import React, {Component} from 'react';
import {connect} from 'react-redux';
import {toast} from 'react-toastify';
import LoginPage from './LoginPageComponent';
import {logIn} from '../../actions/userActions';

class LoginPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isSubmitting: false
        };  
    }

    handleLogin = payload => {
        this.setState({
            isSubmitting: true
        });
        return this.props.logIn(payload)
        .then(() => {
            this.setState({
                isSubmitting: false,
            });
            this.props.history.push("/");
        })
        .catch((e) => {
            this.setState({
                isSubmitting: false,
            });
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
                });
        })
    }

    render() {
        return (
            <>
                {this.props.isLogged 
                    ? this.props.history.push("/")
                    : <LoginPage onSubmit={this.handleLogin} isSubmitting={this.state.isSubmitting} />     
                }
            </> 
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