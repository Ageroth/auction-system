import React, { Component } from 'react';
import UserListPage from './UserListPageComponent'
import { getUsersRequest } from '../../utils/api';
import { toast } from 'react-toastify';

export default class UserListPageContainer extends Component { 
    state = {
      data: [],
      pagination: {
        current: 1
      },
      isLoading: false
    };

    componentDidMount() {
      const { pagination } = this.state;
      this.getUsers({ pagination });
    }

    getUsers = (params = {}) => {
      this.setState({ isLoading: true });

      getUsersRequest(params).then(response => {
        this.setState({
          isLoading: false,
          data: response.data.users,
          pagination: {
            ...params.pagination,
            total: response.data.totalPages,
          }
        });
      }).catch((e) => {
        this.setState({ isLoading: false })
        console.log(e);
      });
    }
    
    render() {
      return (
        <UserListPage data={this.state.data} pagination={this.state.pagination} isLoading={this.state.isLoading} handleTableChange={this.getUsers} />
      );
    }
}