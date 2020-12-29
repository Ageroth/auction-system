import React, {Component} from 'react';
import BiddingTablePage from './BiddingTablePageComponent'
import {toast} from 'react-toastify';
import {getOwnBiddingsRequest} from '../../../utils/api';

export default class BiddingTablePageContainer extends Component {
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

        this.getBiddings({pagination, status, sortField, order});
    }

    getBiddings = (params = {}) => {
        this.setState({isLoading: true});

        getOwnBiddingsRequest(params).then(res => {
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
            <BiddingTablePage handleTableChange={this.getBiddings} {...this.state}/>
        );
    }
}