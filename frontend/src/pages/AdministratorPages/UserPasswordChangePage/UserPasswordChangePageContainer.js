import React, {Component} from 'react';
import UserPasswordChangePage from './UserPasswordChangePageComponent';
import {changeUserPasswordRequest, getUserDetailsRequest} from '../../../utils/api';
import {toast} from 'react-toastify';

export default class UserPasswordChangePageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            isSubmitting: false
        };
    }

    componentDidMount() {
        this.getUserDetails();
    }

    getUserDetails = () => {
        getUserDetailsRequest(this.state.userId).catch(() => {
        });
    }

    handleUserPasswordChange = (payload) => {
        this.setState({isSubmitting: true});

        changeUserPasswordRequest(this.state.userId, payload).then(res => {
            this.setState({isSubmitting: false});
            this.props.history.goBack();
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        }).catch(e => {
            this.setState({isSubmitting: false});
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        });
    }

    render() {
        const onSubmit = this.handleUserPasswordChange;
        const isSubmitting = this.state.isSubmitting;
        return (
            <UserPasswordChangePage onSubmit={onSubmit} isSubmitting={isSubmitting}/>
        );
    }
}