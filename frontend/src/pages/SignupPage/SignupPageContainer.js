import React, {Component} from 'react';
import {connect} from 'react-redux';
import SignupPage from './SignupPageComponent';
import {signUpRequest} from '../../utils/api';

class SignupPageContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isSubmitting: false
        };  
    }

    handleSignup = payload => {
        this.setState({
            isSubmitting: true
        });
        return signUpRequest(payload)
        .then(() => {
            this.setState({
                isSubmitting: false,
            });
            this.props.history.push("/login");
        })
        .catch(() => {
            this.setState({
                isSubmitting: false,
            });
        })
    }

    render() {
        return (
            <>
                {this.props.isLogged
                    ? this.props.history.push("/")
                    : <SignupPage onSubmit={this.handleSignup} isSubmitting={this.state.isSubmitting} />
                }
            </>
        );
    }
}

const mapStateToProps = state => {
    return {
        isLogged: state.user.isLogged
    }
}

export default connect(mapStateToProps,null)(SignupPageContainer);