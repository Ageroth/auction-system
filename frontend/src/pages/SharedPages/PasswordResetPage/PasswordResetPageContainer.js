import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
import {connect} from 'react-redux';
import PasswordResetPage from './PasswordResetPageComponent';
import {getUserRequest, resetPasswordRequest, sendPasswordResetEmailRequest} from '../../../utils/api';
import {toast} from 'react-toastify';

class PasswordResetPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            passwordResetCode: this.props.match.params.passwordResetCode,
            emailSent: false,
            isSubmitting: false,
            version: null
        };
    }

    componentDidMount() {
        if (this.state.passwordResetCode)
            this.getUser()
    }

    getUser = () => {
        getUserRequest(this.state.passwordResetCode).then(res => {
            const eTagValue = res.headers.etag

            this.setState({version: eTagValue});
        }).catch(() => {
            this.props.history.goBack();
        });
    }

    handlePasswordResetEmailSending = (payload) => {
        this.setState({isSubmitting: true});

        sendPasswordResetEmailRequest(payload).then(() => {
            this.setState({
                isSubmitting: false,
                emailSent: true
            });
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    handlePasswordReset = (payload) => {
        resetPasswordRequest(this.state.passwordResetCode, payload, this.state.version).then((res) => {
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });

            this.props.history.push("/login");
        });
    }

    render() {
        const passwordResetCode = this.state.passwordResetCode;
        const isSubmitting = this.state.isSubmitting;

        return (
            <>  {this.props.isLoggedIn ? (
                <Redirect to="/"/>
            ) : (
                <>
                    {this.state.passwordResetCode
                        ? <PasswordResetPage onSubmit={this.handlePasswordReset} passwordResetCode={passwordResetCode}
                                             isSubmitting={isSubmitting}/>
                        : <PasswordResetPage onSubmit={this.handlePasswordResetEmailSending}
                                             emailSent={this.state.emailSent}
                                             passwordResetCode={passwordResetCode} isSubmitting={isSubmitting}/>
                    }
                </>
            )}
            </>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        isLoggedIn: state.user.isLoggedIn
    };
}

export default connect(mapStateToProps, null)(PasswordResetPageContainer);