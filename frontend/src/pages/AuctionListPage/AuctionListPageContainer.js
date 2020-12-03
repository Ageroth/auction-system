import React, {Component} from 'react';
import AuctionListPage from './AuctionListPageComponent'
import {toast} from 'react-toastify';
import {getAuctionsRequest} from '../../utils/api';

export default class AuctionListPageContainer extends Component {
    state = {
        data: [],
        pagination: {
            current: 1,
        },
        isLoading: false
    };

    componentDidMount() {
        const {pagination} = this.state;
        this.getAuctions({pagination});
    }

    getAuctions = (params = {}) => {
        this.setState({isLoading: true});

        getAuctionsRequest(params).then(res => {
            this.setState({
                isLoading: false,
                data: res.data.auctions,
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
            <AuctionListPage data={this.state.data} pagination={this.state.pagination} isLoading={this.state.isLoading}
                             handleTableChange={this.getAuctions}/>
        );
    }
}