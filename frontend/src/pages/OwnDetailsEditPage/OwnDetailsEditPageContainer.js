import React, {Component} from 'react';
import OwnDetailsEditPage from './OwnDetailsEditPageComponent';
import {toast} from 'react-toastify';
import {getMyDetailsRequest, updateOwnDetailsRequest} from '../../utils/api';

export default class OwnDetailsEditPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            myDetails: null,
            isSubmitting: false
        };
    }

    componentDidMount() {
        this.getMyDetails();
    }

    getMyDetails = () => {
        getMyDetailsRequest().then(res => {
            this.setState({myDetails: res.data});
        }).catch((e) => {
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
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
        }).catch(e => {
            this.setState({isSubmitting: false});
            toast.error(e.response.data.message, {
                position: "bottom-right",
                autoClose: 3000,
                closeOnClick: true
            });
        })
    }

    render() {
        const myDetails = this.state.myDetails;
        const isSubmitting = this.state.isSubmitting;
        return (
            <OwnDetailsEditPage myDetails={myDetails} onSubmit={this.handleEdit} isSubmitting={isSubmitting}/>
        );
    }
}