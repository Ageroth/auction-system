import React, {Component} from 'react';
import UserDetailsEditPage from './UserDetailsEditPageComponent';
import {toast} from 'react-toastify';
import {getAllAccessLevelsRequest, getUserDetailsRequest, updateUserDetailsRequest} from '../../../utils/api';

export default class UserDetailsEditPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            userDetails: null,
            accessLevels: [],
            isSubmitting: false
        };
    }

    componentDidMount() {
        this.getUserDetails();
        this.getAllAccessLevels();
    }

    getAllAccessLevels = () => {
        getAllAccessLevelsRequest().then(res => {
            this.setState({accessLevels: res.data});
        });
    }

    getUserDetails = () => {
        getUserDetailsRequest(this.state.userId).then(res => {
            this.setState({userDetails: res.data});
        });
    }

    handleEdit = (payload) => {
        this.setState({isSubmitting: true});
        updateUserDetailsRequest(this.state.userId, payload).then(res => {
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
        const userDetails = this.state.userDetails;
        const accessLevels = this.state.accessLevels;
        const isSubmitting = this.state.isSubmitting;

        return (
            <UserDetailsEditPage userDetails={userDetails} accessLevels={accessLevels}
                                 onSubmit={this.handleEdit} isSubmitting={isSubmitting}/>
        );
    }
}