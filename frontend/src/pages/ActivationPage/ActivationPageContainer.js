import React, { Component } from 'react';
import ActivationPage from './ActivationPageComponent';
import { activateUserRequest } from '../../utils/api';


class ActivationPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            activationCode: this.props.match.params.activationCode,
            error: null
        };  
    }

    componentDidMount() {
        if(this.state.activationCode) {
            this.activateUser();
        } 
      }

    activateUser = () => {
        return activateUserRequest(this.state.activationCode)
            .then(() => {
                this.setState({
                    error: false
                });
            })
            .catch(() => {
                this.setState({
                    error: true
                });
            });
    }

    render() {
        return (
            <ActivationPage error={this.state.error} />
        );
      }
}

export default ActivationPageContainer;