import React, {Component} from 'react';
import {connect} from 'react-redux';

class PrivateRoute extends Component {
  haveAccessToRoute = () => {
    let result = false;
    if (this.props.canAccess) {
        if (this.props.canAccess.includes(this.props.currentRole)) {
          result = true;
        }
    } else {
      result = true;
    }
    return result;
  };

  render() {
    return (
        <>
            {this.props.isLogged 
                ? this.props.history.push("/")
                : this.props.history.push("/login")    
            }
        </> 
    );
  }
}

const mapStateToProps = state => {
    return {
        isLogged: state.user.isLogged,
        currentRole: state.user.currentRole
    }
}

export default connect(mapStateToProps,null)(PrivateRoute);