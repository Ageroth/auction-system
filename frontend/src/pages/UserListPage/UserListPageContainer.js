import React, { Component } from 'react';
import UserListPage from './UserListPageComponent'
import { getUsersRequest } from '../../utils/api';

export default class UserListPageContainer extends Component { 
    state = {
      data: [],
      pagination: {
        current: 1,
      },
      isLoading: false
    };

    componentDidMount() {
      const { pagination } = this.state;
      this.getUsers({ pagination });
    }

    getUsers = (params = {}) => {
      this.setState({ isLoading: true });

      getUsersRequest(params).then(res => {
        this.setState({
          isLoading: false,
          data: res.data.users,
          pagination: {
            ...params.pagination,
            total: res.data.totalItems,
          }
        });
      }).catch(() => {
        this.setState({ isLoading: false })
      });
    }
    
    render() {
      return (
        <UserListPage data={this.state.data} pagination={this.state.pagination} isLoading={this.state.isLoading} handleTableChange={this.getUsers} />
      );
    }
}