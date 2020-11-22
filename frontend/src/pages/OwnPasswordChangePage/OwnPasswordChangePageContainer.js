import React, {Component} from 'react';
import OwnPasswordChangePage from './OwnPasswordChangePageComponent';
import {changeOwnPasswordRequest} from '../../utils/api';
import {toast} from 'react-toastify';

export default class OwnPasswordChangePageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isSubmitting: false
        };
    }

    handleOwnPasswordChange = (payload) => {
        this.setState({isSubmitting: true});
        changeOwnPasswordRequest(payload).then(res => {
            this.setState({isSubmitting: false});
            this.props.history.goBack();
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        }).catch(e => {
            this.setState({isSubmitting: false});
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        });
    }

    render() {
        const onSubmit = this.handleOwnPasswordChange;
        const isSubmitting = this.state.isSubmitting;
        return (
            <OwnPasswordChangePage onSubmit={onSubmit} isSubmitting={isSubmitting}/>
        );
    }
}