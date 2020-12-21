import React, {Component} from 'react';
import UserTablePage from './UserTablePageComponent'
import {toast} from 'react-toastify';
import {getUsersRequest} from '../../../utils/api';

export default class UserTablePageContainer extends Component {
    state = {
        users: [],
        pagination: {
            current: 1,
        },
        isLoading: false
    };

    componentDidMount() {
        const {pagination} = this.state;

        this.getUsers({pagination});
    }

    getUsers = (params = {}) => {
        this.setState({isLoading: true});

        getUsersRequest(params).then(res => {
            this.setState({
                isLoading: false,
                users: res.data.users,
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
            <UserTablePage handleTableChange={this.getUsers} {...this.state}/>
        );
    }
}