import React, {Component} from 'react';
import UserAddPage from './UserAddPageComponent'
import {toast} from 'react-toastify';
import {addUserRequest, getAllAccessLevelsRequest} from '../../../utils/api';

export default class UserAddPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isSubmitting: false,
            accessLevels: []
        };
    }

    componentDidMount() {
        this.getAllAccessLevels();
    }

    getAllAccessLevels = () => {
        getAllAccessLevelsRequest().then(res => {
            this.setState({accessLevels: res.data});
        }).catch(() => {
        });
    }

    handleAdd = (payload) => {
        this.setState({isSubmitting: true});

        addUserRequest(payload).then(res => {
            this.setState({isSubmitting: false});
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });

            this.props.history.push('/users')
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    render() {
        return (
            <UserAddPage accessLevels={this.state.accessLevels} onSubmit={this.handleAdd}
                         isSubmitting={this.state.isSubmitting}/>
        );
    }
}