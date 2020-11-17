import React, { Component } from 'react';
import PasswordChangePage from './PasswordChangePageComponent';
import NotFoundPage from '../NotFoundPage'
import { changeUserPasswordRequest } from '../../utils/api';
import { toast } from 'react-toastify';

export default class PasswordChangePageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            isSubmitting: null,
            error: false,
        };  
    }

    handleUserPasswordChange = (payload) => {
        this.setState({ isSubmitting: true });

        changeUserPasswordRequest(this.state.userId, payload).then(res => {
            this.setState({ isSubmitting: false });
            
            this.props.history.goBack();
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        }).catch(e => {
            this.setState({ 
                isSubmitting: false,
                error: true
            });
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
                {this.state.error ? <NotFoundPage/> : <PasswordChangePage onSubmit={this.handleUserPasswordChange} isSubmitting={this.state.isSubmitting} />}
            </>
        );
      }
}