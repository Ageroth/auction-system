import React, {Component} from 'react';
import OwnAuctionTablePage from './OwnAuctionTablePageComponent'
import {getOwnAuctionsRequest} from '../../../utils/api';

export default class OwnAuctionTablePageContainer extends Component {
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
        }).catch(() => {
            this.setState({isLoading: false})
        });
    }

    render() {
        return (
            <OwnAuctionTablePage handleTableChange={this.getOwnAuctions} {...this.state}/>
        );
    }
}