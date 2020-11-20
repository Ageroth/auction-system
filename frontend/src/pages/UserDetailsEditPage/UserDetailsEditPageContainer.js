import React, { Component } from 'react';
import UserDetailsEditPage from './UserDetailsEditPageComponent';
import NotFoundPage from '../NotFoundPage'
import { toast } from 'react-toastify';
import { getUserDetailsRequest, updateUserDetailsRequest } from '../../utils/api';

export default class UserDetailsEditPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            userDetails: null,
            isSubmitting: false,
            error: false
        };  
    } 
    
    componentDidMount() {
        this.getUserDetails();
    }

    getUserDetails = () => {
        getUserDetailsRequest(this.state.userId).then(res => {
            this.setState({ userDetails: res.data });
        }).catch(e => {
            this.setState({ error: true });
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        });
    }

    handleEdit = (payload) => {
        this.setState({ isSubmitting: true });
        updateUserDetailsRequest(this.state.userId, payload).then((res) => {
            this.setState({ isSubmitting: false });
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
            this.props.history.goBack();
        }).catch(e => {
            this.setState({ isSubmitting: false });
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        })
    }

    render() {
        const userDetails = this.state.userDetails;
        const isSubmitting = this.state.isSubmitting;
        return (
            <>
                {this.state.error ? <NotFoundPage/> : <UserDetailsEditPage userDetails={userDetails} onSubmit={this.handleEdit} isSubmitting={isSubmitting} />}
            </>
        );
    }
}