import React, { Component } from 'react';
import { Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import ActivationPage from './ActivationPageComponent';
import { activateUserRequest } from '../../utils/api';


class ActivationPageContainer extends Component {
    state = {
        activationCode: this.props.match.params.activationCode,
        error: null
    };  
    
    componentDidMount() {
        if(this.state.activationCode) this.activateUser();
    }

    activateUser = () => {
        activateUserRequest(this.state.activationCode).then(() => {
            this.setState({ error: false });
        }).catch(() => {
            this.setState({ error: true });
        });
    }

    render() {
        return (
            <>
                {this.props.isLoggedIn
                    ? <Redirect to="/" />
                    : <ActivationPage error={this.state.error} />
                }
            </>
        );
    }
}

const mapStateToProps = state => {
    return {
        isLoggedIn: state.user.isLoggedIn
    };
}

export default connect(mapStateToProps, null)(ActivationPageContainer);