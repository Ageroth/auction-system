import React, {Component} from 'react';
import {Redirect} from 'react-router-dom';
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

    handleSignup = (payload) => {
        this.setState({isSubmitting: true});

        return signUpRequest(payload).then(() => {
            this.setState({isSubmitting: false});
            this.props.history.push("/login");
        }).catch(() => {
            this.setState({isSubmitting: false});
        });
    }

    render() {
        return (
            <>
                {this.props.isLoggedIn
                    ? <Redirect to="/"/>
                    : <SignupPage onSubmit={this.handleSignup} isSubmitting={this.state.isSubmitting}/>
                }
            </>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        isLoggedIn: state.user.isLoggedIn
    };
}

export default connect(mapStateToProps, null)(SignupPageContainer);