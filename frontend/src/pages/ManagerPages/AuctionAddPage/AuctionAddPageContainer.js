import React, {Component} from 'react';
import AuctionAddPage from './AuctionAddPageComponent'
import {toast} from 'react-toastify';
import {addAuctionRequest} from '../../../utils/api';

export default class AuctionAddPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isSubmitting: false
        };
    }

    handleAdd = (payload) => {
        this.setState({isSubmitting: true});
        addAuctionRequest(payload).then(res => {
            this.setState({isSubmitting: false});
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });

            this.props.history.push(`../my_auctions`);
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
        return (
            <AuctionAddPage onSubmit={this.handleAdd} isSubmitting={this.state.isSubmitting}/>
        );
    }
}