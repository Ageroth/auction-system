import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {connect} from 'react-redux';
import {compose} from "redux";
import SignupPage from './SignupPageComponent';
import {signUpRequest} from '../../../utils/api';
import {withTranslation} from 'react-i18next';
import {toast} from "react-toastify";

class SignupPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isSubmitting: false,
        };

    }

    handleSignup = (payload) => {
        this.setState({isSubmitting: true});

        signUpRequest(payload).then(() => {
            this.setState({isSubmitting: false});
            toast.success(this.props.t('message.content.activationEmailSent'), {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
            this.props.history.push("/login");
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    render() {
        return (
            <>
                {this.props.isLoggedIn
                    ? <Redirect to="/"/>
                    : <SignupPage onSubmit={this.handleSignup} isSubmitting={this.state.isSubmitting}/>
                }
            </>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        isLoggedIn: state.user.isLoggedIn
    };
}

export default compose(withTranslation(), connect(mapStateToProps, null))(SignupPageContainer);