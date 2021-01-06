import React, {Component} from 'react';
import AuctionTablePage from './AuctionTablePageComponent'
import {getAuctionsRequest} from '../../../utils/api';

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

        this.getAuctions({pagination, status, sortField, order});
    }

    getAuctions = (params = {}) => {
        this.setState({isLoading: true});

        getAuctionsRequest(params).then(res => {
            this.setState({
                isLoading: false,
                auctions: res.data.auctions,
                pagination: {
                    ...params.pagination,
                    total: res.data.totalItems,
                }
            });
        }).catch(() => {
            this.setState({isLoading: false})
        });
    }

    render() {
        return (
            <AuctionTablePage handleTableChange={this.getAuctions} {...this.state}/>
        );
    }
}