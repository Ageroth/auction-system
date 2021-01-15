import React, {Component} from 'react';
import OwnAuctionDetailsPage from './OwnAuctionDetailsPageComponent';
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {toast} from 'react-toastify';
import {deleteAuctionRequest, getOwnAuctionDetailsRequest} from '../../../utils/api';

export default class OwnAuctionDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            auctionId: this.props.match.params.auctionId,
            auctionDetails: null,
            isSubmitting: false,
            version: null,
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
            return new SockJS('http://localhost:8080/ws');
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
        getOwnAuctionDetailsRequest(this.state.auctionId).then((res) => {
            const eTagValue = res.headers.etag

            this.setState({auctionDetails: res.data, version: eTagValue});
        }).catch(() => {
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

            this.props.history.push("/my_auctions");
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    render() {
        const auctionDetails = this.state.auctionDetails;
        const isSubmitting = this.state.isSubmitting;
        const handleDelete = this.deleteAuction;

        return (
            <OwnAuctionDetailsPage auctionDetails={auctionDetails} handleDelete={handleDelete}
                                   isSubmitting={isSubmitting}/>
        );
    }
}