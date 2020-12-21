import React, {Component} from 'react';
import OwnAuctionTablePage from './OwnAuctionTablePageComponent'
import {toast} from 'react-toastify';
import {getOwnAuctionsRequest} from '../../../utils/api';

export default class AuctionTablePageContainer extends Component {
    state = {
        auctions: [],
        pagination: {
            current: 1,
        },
        isLoading: false
    };

    componentDidMount() {
        const status = "CURRENT";
        const sortField = "startDate";
        const order = "ascend";
        const {pagination} = this.state;

        this.getOwnAuctions({pagination, status, sortField, order});
    }

    getOwnAuctions = (params = {}) => {
        this.setState({isLoading: true});

        getOwnAuctionsRequest(params).then(res => {
            this.setState({
                isLoading: false,
                auctions: res.data.auctions,
                pagination: {
                    ...params.pagination,
                    total: res.data.totalItems,
                }
            });
        }).catch(e => {
            this.setState({isLoading: false})
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        });
    }

    render() {
        return (
            <OwnAuctionTablePage handleTableChange={this.getOwnAuctions} {...this.state}/>
        );
    }
}