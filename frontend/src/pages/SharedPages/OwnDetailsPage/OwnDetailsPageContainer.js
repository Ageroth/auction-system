import React, {Component} from 'react';
import OwnDetailsPage from './OwnDetailsPageComponent';
import {getOwnDetailsRequest} from '../../../utils/api';

export default class OwnDetailsPageContainer extends Component {
    state = {
        myDetails: null,
    };

    componentDidMount() {
        this.getOwnDetails();
    }

    getOwnDetails = () => {
        getOwnDetailsRequest().then(res => {
            this.setState({myDetails: res.data});
        }).catch();
    }

    render() {
        return (
            <OwnDetailsPage myDetails={this.state.myDetails}/>
        );
    }
}