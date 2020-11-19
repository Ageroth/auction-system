import React, { Component } from 'react';
import UserDetailsPage from './UserDetailsPageComponent';
import NotFoundPage from '../NotFoundPage'
import { toast } from 'react-toastify';
import { getUserDetailsRequest } from '../../utils/api';


export default class UserDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: this.props.match.params.userId,
            userDetails: null,
            error: false
        };  
    } 
    
    componentDidMount() {
        this.getUserDetails();
    }

    getUserDetails = () => {
        getUserDetailsRequest(this.state.userId).then(res => {
            this.setState({userDetails: res.data});
        }).catch((e) => {
            this.setState({error: true});
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
                {this.state.error ? <NotFoundPage/> : <UserDetailsPage userDetails={this.state.userDetails} />}
            </>
        );
    }
}