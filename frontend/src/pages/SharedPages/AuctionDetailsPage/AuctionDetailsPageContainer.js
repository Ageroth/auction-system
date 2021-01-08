import React, {Component} from 'react';
import AuctionDetailsPage from './AuctionDetailsPageComponent';
import {toast} from 'react-toastify';
import {deleteAuctionRequest, getAuctionDetailsRequest, placeABidRequest} from '../../../utils/api';

export default class AuctionDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            auctionId: this.props.match.params.auctionId,
            auctionDetails: null,
            isSubmitting: false,
            version: null
        };
    }

    componentDidMount() {
        this.getAuctionDetails();
    }

    getAuctionDetails = () => {
        getAuctionDetailsRequest(this.state.auctionId).then((res) => {
            const eTagValue = res.headers.etag

            this.setState({auctionDetails: res.data, version: eTagValue});
        }).catch();
    }

    placeBid = (payload) => {
        this.setState({isSubmitting: true});

        placeABidRequest(this.state.auctionId, payload).then((res) => {
            this.setState({isSubmitting: false});

            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    deleteAuction = () => {
        this.setState({isSubmitting: true});

        deleteAuctionRequest(this.state.auctionId, this.state.version).then((res) => {
            this.setState({isSubmitting: false});

            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });

            this.props.history.push("/auctions");
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    render() {
        const auctionDetails = this.state.auctionDetails;
        const isSubmitting = this.state.isSubmitting;
        const handleBidPlace = this.placeBid;
        const handleDelete = this.deleteAuction;

        return (
            <AuctionDetailsPage auctionDetails={auctionDetails} handleBidPlace={handleBidPlace}
                                handleDelete={handleDelete}
                                isSubmitting={isSubmitting}/>
        );
    }
}