import React from 'react';
import AppLayout from '../../../components/AppLayout/AppLayout'
import logo from '../../../assets/logo.png'

const HomePage = () => {
    return (
        <AppLayout>
            <div className="home-page-wrapper">
                <img src={logo} alt="logo"/>
            </div>
        </AppLayout>
    )
}

export default HomePage