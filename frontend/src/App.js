import React, {Fragment} from 'react';
import {ToastContainer} from 'react-toastify';
import MyRouter from './hocs/MyRouter/MyRouter';
import 'react-toastify/dist/ReactToastify.css';


const App = () => {
    return (
        <Fragment>
            <MyRouter/>
            <ToastContainer />
        </Fragment>
    );
}

export default App;