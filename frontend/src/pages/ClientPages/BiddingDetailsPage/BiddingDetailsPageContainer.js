import React, {Component} from 'react';
import BiddingDetailsPage from './BiddingDetailsPageComponent';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {toast} from 'react-toastify';
import {getOwnBiddingDetailsRequest, placeABidRequest} from '../../../utils/api';

export default class BiddingDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            auctionId: this.props.match.params.auctionId,
            auctionDetails: null,
            isSubmitting: false,
            stompClient: null
        };
    }

    componentDidMount() {
        this.getAuctionDetails();
        this.initSocket();
    }

    componentWillUnmount() {
        this.state.stompClient.deactivate().then(() => {
        });
    }

    initSocket() {
        const auctionId = this.state.auctionId;
        const self = this;
        const stompClient = Stomp.client();

        stompClient.webSocketFactory = () => {
            return new SockJS('https://localhost:8443/ws');
        }

        stompClient.onConnect = () => {
            stompClient.subscribe(`/auction/changes/${auctionId}`, () => {
                self.getAuctionDetails();
            });
        }

        stompClient.activate();

        this.setState({stompClient: stompClient});
    }

    getAuctionDetails = () => {
        getOwnBiddingDetailsRequest(this.state.auctionId).then((res) => {
            this.setState({auctionDetails: res.data});
        }).catch(() => {
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
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
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