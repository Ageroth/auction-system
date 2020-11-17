import React, { Component } from 'react';
import UserDetailsPage from './UserDetailsPageComponent';
import NotFoundPage from '../NotFoundPage'
import { getUserDetailsRequest } from '../../utils/api';


export default class UserDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            userDetails: null,
            error: false,
        };  
    } 
    
    componentDidMount() {
        this.getUserDetails();
    }

    getUserDetails = () => {
        getUserDetailsRequest(this.state.userId).then(res => {
            this.setState({
                userDetails: res.data
            });
        }).catch(() => {
            this.setState({
                error: true
            });
        });
    }

    render() {
        return (
            <>
                {this.state.error ? <NotFoundPage/> : <UserDetailsPage userDetails={this.state.userDetails} />}
            </>
        );
    }
}