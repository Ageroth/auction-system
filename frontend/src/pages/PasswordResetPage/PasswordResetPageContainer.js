import React, { Component } from 'react';
import PasswordResetPage from './PasswordResetPageComponent';
import { sendPasswordResetEmailRequest, resetPasswordRequest } from '../../utils/api';
import { toast } from 'react-toastify';

export default class PasswordResetPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            passwordResetCode: this.props.match.params.passwordResetCode,
            emailSent: false,
            isSubmitting: false
        };  
    }

    handlePasswordResetEmailSending = (payload) => {
        this.setState({ isSubmitting: true });

        sendPasswordResetEmailRequest(payload).then(() => {
            this.setState({
                isSubmitting: false,
                emailSent: true
            });
        }).catch(e => {
            this.setState({ isSubmitting: false });
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        });
    }

    handlePasswordReset = (payload) => {
        resetPasswordRequest(this.state.passwordResetCode, payload).then((res) => {
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: false,
                closeOnClick: true
            });
            this.props.history.push("/login");
        }).catch((e) => {
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        });
    }

    render() {
        return (
            <>
                {this.state.passwordResetCode
                    ? <PasswordResetPage onSubmit={this.handlePasswordReset} passwordResetCode={this.state.passwordResetCode} isSubmitting={this.state.isSubmitting} />
                    : <PasswordResetPage onSubmit={this.handlePasswordResetEmailSending} emailSent={this.state.emailSent}
                        passwordResetCode={this.state.passwordResetCode} isSubmitting={this.state.isSubmitting} />
                }
            </>
        );
      }
}