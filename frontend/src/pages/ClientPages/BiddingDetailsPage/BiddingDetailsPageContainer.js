import React, {Component} from 'react';
import BiddingDetailsPage from './BiddingDetailsPageComponent';
import {toast} from 'react-toastify';
import {getOwnBiddingDetailsRequest, placeABidRequest} from '../../../utils/api';

export default class BiddingDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            auctionId: this.props.match.params.auctionId,
            auctionDetails: null,
            isSubmitting: false
        };
    }

    componentDidMount() {
        this.getAuctionDetails();
    }

    getAuctionDetails = () => {
        getOwnBiddingDetailsRequest(this.state.auctionId).then((res) => {
            this.setState({auctionDetails: res.data});
        });
    }

    handleBidPlace = (payload) => {
        this.setState({isSubmitting: true});
        placeABidRequest(this.state.auctionId, payload).then(res => {
            this.setState({isSubmitting: false});
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
        })
    }

    render() {
        const auctionDetails = this.state.auctionDetails;
        const isSubmitting = this.state.isSubmitting;

        return (
            <BiddingDetailsPage auctionDetails={auctionDetails} onSubmit={this.handleBidPlace}
                                isSubmitting={isSubmitting}/>
        );
    }

}