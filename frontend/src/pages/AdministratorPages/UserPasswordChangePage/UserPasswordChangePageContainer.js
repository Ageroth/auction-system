import React, {Component} from 'react';
import UserPasswordChangePage from './UserPasswordChangePageComponent';
import {changeUserPasswordRequest, getUserDetailsRequest} from '../../../utils/api';
import {toast} from 'react-toastify';

export default class UserPasswordChangePageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            isSubmitting: false,
            version: null
        };
    }

    componentDidMount() {
        this.getUserDetails();
    }

    getUserDetails = () => {
        getUserDetailsRequest(this.state.userId).then(res => {
            const eTagValue = res.headers.etag

            this.setState({version: eTagValue});
        }).catch(() => {
        });
    }

    handleUserPasswordChange = (payload) => {
        this.setState({isSubmitting: true});

        changeUserPasswordRequest(this.state.userId, payload, this.state.version).then(res => {
            this.setState({isSubmitting: false});

            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });

            this.props.history.goBack();
        }).catch(() => {
            this.setState({isSubmitting: false});
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