import React, {Component} from 'react';
import AuctionDetailsPage from './AuctionDetailsPageComponent';
import NotFoundPage from '../NotFoundPage'
import {toast} from 'react-toastify';
import {getAuctionDetailsRequest} from '../../utils/api';

export default class AuctionDetailsPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            auctionId: this.props.match.params.auctionId,
            auctionDetails: null,
            error: false
        };
    }

    componentDidMount() {
        this.getAuctionDetails();
    }

    getAuctionDetails = () => {
        getAuctionDetailsRequest(this.state.auctionId).then((res) => {
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
        return (
            <>
                {this.state.error ? <NotFoundPage/> : <AuctionDetailsPage auctionDetails={this.state.auctionDetails}/>}
            </>
        );
    }
}