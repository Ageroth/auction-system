import React, {Component} from 'react';
import UserDetailsPage from './UserDetailsPageComponent';
import {getUserDetailsRequest} from '../../../utils/api';

export default class UserDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            userDetails: null
        };
    }

    componentDidMount() {
        this.getUserDetails();
    }

    getUserDetails = () => {
        getUserDetailsRequest(this.state.userId).then((res) => {
            this.setState({userDetails: res.data});
        }).catch();
    }

    render() {
        return (
            <UserDetailsPage userDetails={this.state.userDetails}/>
        );
    }
}