import React, {Component} from 'react';
import OwnPasswordChangePage from './OwnPasswordChangePageComponent';
import {changeOwnPasswordRequest, getOwnDetailsRequest} from '../../../utils/api';
import {toast} from 'react-toastify';

export default class OwnPasswordChangePageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isSubmitting: false,
            version: null
        };
    }

    componentDidMount() {
        this.getOwnDetails();
    }

    getOwnDetails = () => {
        getOwnDetailsRequest().then(res => {
            const eTagValue = res.headers.etag

            this.setState({version: eTagValue});
        }).catch(() => {
        });
    }

    handleOwnPasswordChange = (payload) => {
        this.setState({isSubmitting: true});

        changeOwnPasswordRequest(payload, this.state.version).then(res => {
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
        const onSubmit = this.handleOwnPasswordChange;
        const isSubmitting = this.state.isSubmitting;
        return (
            <OwnPasswordChangePage onSubmit={onSubmit} isSubmitting={isSubmitting}/>
        );
    }
}