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

    getUsers = (params) => {
      this.setState({ isLoading: true });

      getUsersRequest(params).then(response => {
        const users = response.data.users.map(row => ({
          key: row.id,
          id: row.id,
          username: row.username,
          email: row.email,
          activated: row.activated,
          firstName: row.firstName,
          lastName: row.lastName
        }));

        this.setState({
          isLoading: false,
          data: users,
          pagination: {
            ...params.pagination,
            total: response.data.totalPages,
          }
        });
      }).catch((e) => {
        console.log(e);
      });
    }
    
    render() {
      return (
        <UserListPage data={this.state.data} pagination={this.state.pagination} isLoading={this.state.isLoading} />
      );
    }
}