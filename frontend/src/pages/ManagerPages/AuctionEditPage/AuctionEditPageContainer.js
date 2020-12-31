import React, {Component} from 'react';
import AuctionEditPage from './AuctionEditPageComponent'
import {toast} from 'react-toastify';
import {getOwnAuctionDetailsRequest, updateAuctionRequest} from "../../../utils/api";
import NotFoundPage from "../../SharedPages/NotFoundPage";

export default class AuctionEditPageContainer extends Component {
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

    handleEdit = (payload) => {
        this.setState({isSubmitting: true});
        updateAuctionRequest(this.state.auctionId, payload).then(res => {
            this.setState({isSubmitting: false});
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });

            this.props.history.goBack();
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
            <>
                {this.state.error ? <NotFoundPage/> :
                    <AuctionEditPage auctionDetails={auctionDetails} onSubmit={this.handleEdit}
                                     isSubmitting={isSubmitting}/>}
            </>
        );
    }
}