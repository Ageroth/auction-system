import React, {Component} from 'react';
import OwnDetailsEditPage from './OwnDetailsEditPageComponent';
import {toast} from 'react-toastify';
import {getOwnDetailsRequest, updateOwnDetailsRequest} from '../../../utils/api';

export default class OwnDetailsEditPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            myDetails: null,
            isSubmitting: false
        };
    }

    componentDidMount() {
        this.getOwnDetails();
    }

    getOwnDetails = () => {
        getOwnDetailsRequest().then(res => {
            this.setState({myDetails: res.data});
        });
    }

    handleEdit = (payload) => {
        this.setState({isSubmitting: true});
        updateOwnDetailsRequest(payload).then((res) => {
            this.setState({isSubmitting: false});
            toast.success(res.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
            this.props.history.goBack();
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    render() {
        const myDetails = this.state.myDetails;
        const isSubmitting = this.state.isSubmitting;
        return (
            <OwnDetailsEditPage myDetails={myDetails} onSubmit={this.handleEdit} isSubmitting={isSubmitting}/>
        );
    }
}