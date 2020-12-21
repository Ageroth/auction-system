import React, {Component} from 'react';
import OwnAuctionDetailsPage from './OwnAuctionDetailsPageComponent';
import NotFoundPage from '../../SharedPages/NotFoundPage'
import {toast} from 'react-toastify';
import {getOwnAuctionDetailsRequest} from '../../../utils/api';

export default class OwnAuctionDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            auctionId: this.props.match.params.auctionId,
            auctionDetails: null,
            isSubmitting: false,
            error: false
        };
    }

    componentDidMount() {
        this.getOwnAuctionDetails();
    }

    getOwnAuctionDetails = () => {
        getOwnAuctionDetailsRequest(this.state.auctionId).then((res) => {
            this.setState({auctionDetails: res.data});
        }).catch(e => {
            this.setState({error: true});
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        });
    }

    render() {
        const auctionDetails = this.state.auctionDetails;
        const isSubmitting = this.state.isSubmitting;

        return (
            <>
                {this.state.error ? <NotFoundPage/> :
                    <OwnAuctionDetailsPage auctionDetails={auctionDetails} isSubmitting={isSubmitting}/>}
            </>
        );
    }
}