import React, {Component} from 'react';
import OwnAuctionDetailsPage from './OwnAuctionDetailsPageComponent';
import {toast} from 'react-toastify';
import {deleteAuctionRequest, getOwnAuctionDetailsRequest} from '../../../utils/api';

export default class OwnAuctionDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            auctionId: this.props.match.params.auctionId,
            auctionDetails: null,
            isSubmitting: false
        };
    }

    componentDidMount() {
        this.getOwnAuctionDetails();
    }

    getOwnAuctionDetails = () => {
        getOwnAuctionDetailsRequest(this.state.auctionId).then((res) => {
            this.setState({auctionDetails: res.data});
        });
    }

    deleteAuction = () => {
        this.setState({isSubmitting: true});

        deleteAuctionRequest(this.state.auctionId).then((res) => {
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