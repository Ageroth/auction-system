import React, { Component } from 'react';
import OwnDetailsPage from './OwnDetailsPageComponent';
import NotFoundPage from '../NotFoundPage'
import { toast } from 'react-toastify';
import { getMyDetailsRequest } from '../../utils/api';


export default class OwnDetailsPageContainer extends Component {
    state = {
        myDetails: null,
    };  

    componentDidMount() {
        this.getMyDetails();
    }

    getMyDetails = () => {
        getMyDetailsRequest().then(res => {
            this.setState({myDetails: res.data});
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
                {this.state.error ? <NotFoundPage/> : <OwnDetailsPage myDetails={this.state.myDetails} />}
            </>
        );
    }
}